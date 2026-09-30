# Similimum AI — Retrieval-Augmented Generation (RAG) Pipeline
`Location: /docs/ai/rag-pipeline.md`

---

## 1. Domain-Specific Ingestion & Chunking Strategy

Homeopathic literature cannot be chunked using naive character-based or fixed-token splitters. Rubrics and Materia Medica keynotes possess strict hierarchical structures that lose clinical meaning if bisected.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       HIERARCHICAL CHUNKING TOPOLOGY                        │
├────────────────────────────────────────┬────────────────────────────────────┤
│ REPERTORY (Kent's / Boenninghausen)    │ MATERIA MEDICA (Boericke / Allen)  │
├────────────────────────────────────────┼────────────────────────────────────┤
│ CHAPTER: Stomach                       │ REMEDY: Bryonia Alba               │
│  └─ RUBRIC: Thirst                     │  ├─ SECTION: Mind & Sensorium      │
│      └─ SUB-RUBRIC: Large quantities,  │  ├─ SECTION: Modalities (< / >)    │
│                     long intervals     │  ├─ SECTION: Physical Generals     │
│         [Remedies: BRY. (3),           │  └─ SECTION: Dose & Relationships  │
│          ARS. (2), PHOS. (2)]          │                                    │
└────────────────────────────────────────┴────────────────────────────────────┘
```

### Chunking Rules:
1. **Repertory Rubric Units**:
   - Each chunk represents exactly **one rubric + complete sub-rubric hierarchy + remedy grades**.
   - Metadata payload: `chapter` (e.g., "MIND"), `rubric_path` (e.g., "MIND - ANGER - sudden, with fright"), `canonical_id`, `grade_3_remedies`, `grade_2_remedies`.
2. **Materia Medica Units**:
   - Split per **anatomical system or modality block** (Mind, Head, Modalities Aggravation, Modalities Amelioration, Keynotes).
   - Chunk size: strictly 200–450 tokens with 50-token semantic overlap to retain contextual continuity.

---

## 2. Hybrid Retrieval Flow (BM25 + Dense Vector)

```
Natural Language Doctor Input:
"Throbbing headache better pressing firmly, dry lips, excessive thirst for cold water"
                              │
             ┌────────────────┴────────────────┐
             ▼                                 ▼
   Dense Vector Pipeline            Sparse Keyword Pipeline
(text-embedding-004, 768d)               (BM25 / FTS5)
             │                                 │
     Top-40 Vector Hits               Top-40 Keyword Hits
             └────────────────┬────────────────┘
                              ▼
                     Reciprocal Rank Fusion
                    (RRF k=60, Weight 0.65v / 0.35k)
                              │
                              ▼
                  Cross-Encoder Reranker
                 (ms-marco-MiniLM-L-6-v2)
                              │
                              ▼
                     Top-5 Filtered Chunks
  1. Kent: STOMACH - THIRST - extreme - cold water, for: BRY. (3), ACON. (2)
  2. Kent: HEAD - PAIN - pressing - pressure amel.: BRY. (3), PULS. (2)
  3. Boericke: Bryonia Alba - Head & Modalities (< motion, > pressure)
```

---

## 3. Context Assembly Contract

Retrieved chunks are formatted into a deterministic context block injected into the LLM system prompt:

```markdown
### VERIFIED CLASSICAL SOURCES CONTEXT (READ-ONLY GROUND TRUTH)
[SOURCE 1: Kent's Repertory | Chapter: HEAD]
Rubric: "PAIN - bursting, stitching - motion, on - agg."
Remedies: BRYONIA (3), BELLADONNA (2), SPIGELIA (2), SULPHUR (1)

[SOURCE 2: Boericke's Materia Medica | Remedy: Bryonia Alba]
Keynotes: "Mucous membranes dry. Stitching, tearing pains worse for least motion, better rest and pressure. Excessive thirst for large quantities of cold water at long intervals."

[SOURCE 3: Allen's Keynotes | Remedy: Nux Vomica]
Differential: "Sedentary habits, irritable, chilly, worse early morning, craves stimulants."
```

---

## 4. Pipeline Guardrails & Hallucination Mitigation

1. **Rubric Existence Verification**: Any remedy proposed by the LLM *must* cross-reference against the pre-loaded SQLite rubric index. If a remedy is suggested that does not appear in the canonical Kent repertory for that rubric, the system strips the suggestion before UI rendering.
2. **Citation Injection**: Every differential candidate card rendered in the Android app must display the exact text citation:
   - Example: `"Bryonia Alba — Grade 3 (Kent Rep. p. 142; Boericke p. 118)"`.

---

## 5. RAG Assumptions

- **[ASSUMPTION-RAG-01]** Hybrid search uses Reciprocal Rank Fusion (RRF) with vector weight 0.65 and BM25 weight 0.35 to prevent semantic drift away from specific homeopathic modalities.
- **[ASSUMPTION-RAG-02]** Embedding vectors are pre-computed and bundled into the Cloud pgvector database; client devices never compute high-dimensional dense embeddings locally.
