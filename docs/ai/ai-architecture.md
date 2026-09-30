# Similimum AI — Clinical AI Architecture Specification
`Location: /docs/ai/ai-architecture.md`

---

## 1. System Vision & AI Philosophy

Similimum AI does **not** replace the physician. In accordance with Hahnemann's *Organon of Medicine* (§1–§3), the physician alone holds the discerning faculties to perceive what is curable in disease and what is curative in medicines. The AI engine acts strictly as an **Ambient Clinical Decision Support (CDS) System**.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       CLINICAL REASONING WORKFLOW                           │
├─────────────────────┬───────────────────────────┬───────────────────────────┤
│ Ambient Speech /    │ Local Deterministic       │ Cloud Hybrid RAG          │
│ Clinical Text Notes │ LSMC & Kent Matrix        │ (Gemini 2.5 Flash +       │
│                     │ (SQLite FTS5 + Room)      │ Vector Embeddings)        │
│         │           │             │             │             │             │
│         ▼           │             ▼             │             ▼             │
│ Real-time Acoustic  │ Instant on-device         │ Deep Differential         │
│ Transcription       │ Rubric Matching           │ Diagnosis, Miasm Analysis │
│ (Whisper / Google)  │ (< 15ms zero latency)     │ & Literature Citations    │
└─────────────────────┴───────────────────────────┴───────────────────────────┘
```

---

## 2. LLM Selection & Benchmark Justification

| Model Candidate | Latency (p90) | Context Window | Structured JSON | Multilingual (EN/HI) | Decision |
|---|---|---|---|---|---|
| **Google Gemini 2.5 Flash** | **620 ms** | **1,000,000 tokens** | **Native Schema Enforcement** | **Tier 1 (Hinglish/Hindi)** | **PRIMARY SELECTION** |
| Gemini 2.5 Pro | 2,100 ms | 2,000,000 tokens | Native Schema Enforcement | Tier 1 | Secondary (Complex Chronic Synthesis) |
| Local Gemma 2B (On-Device) | 1,800 ms | 4,096 tokens | Partial | Limited Hindi | [ASSUMPTION-AI-01] Evaluated for Phase 3 Edge offline inference |

### Selection Rationale:
1. **Gemini 2.5 Flash** delivers sub-second streaming inference essential for real-time consulting room responsiveness.
2. Native support for strictly enforced JSON Schemas (`responseSchema`) eliminates parsing hallucinations when extracting Kentian rubrics and modalities.
3. Unmatched multilingual depth in Indian English, Shuddh Hindi, and Hinglish clinical terminology.

---

## 3. Two-Tier Retrieval Architecture (Local FTS5 + Dense Vector RAG)

To guarantee 100% clinic continuity without internet dependencies, Similimum AI employs a dual-engine retrieval pipeline:

### Tier 1: Local Deterministic Engine (Offline / Instant)
- **Engine**: SQLite FTS5 (Full-Text Search) with BM25 ranking compiled directly inside the Android Room APK.
- **Corpus**: 30 Core Polychrests (Boericke Materia Medica), Kent's 15 Primary Rubrics, and 12 Schuessler Biochemic Salts.
- **Latency**: 8–18 ms on mid-range Android hardware (e.g., MediaTek Dimensity 7050 / Snapdragon 7s Gen 2).

### Tier 2: Cloud Hybrid Dense RAG (Online / Deep Analysis)
- **Embedding Model**: `text-embedding-004` (768 dimensions), optimized for clinical semantic distance.
- **Vector Database**: Cloud-hosted PostgreSQL with `pgvector` index (HNSW with cosine similarity metric `m=16, ef_construction=64`).
- **Corpus**: Complete Boericke's *Pocket Manual of Homeopathic Materia Medica*, Kent's *Repertory of the Homoeopathic Materia Medica* (65,000 rubrics), Allen's *Keynotes*, and Hering's *Guiding Symptoms*.

---

## 4. Context Assembly & Inference Budget

When a doctor triggers AI synthesis or ambient listening completes a sentence:
1. **Acoustic / Note Tokenization**: Doctor input is parsed into Chief Complaint, Modalities, and Mental Generals (~350 tokens).
2. **Dense Semantic Retrieval**: Top 5 candidate rubrics and top 4 Materia Medica keynote excerpts retrieved (~1,200 tokens).
3. **Miasmatic Prior Context**: Patient miasmatic background (Psora, Sycosis, Syphilis, Tubercular) and constitution injected (~200 tokens).
4. **Guardrail Enclosure**: Mandatory decision-support system framing prepended (~400 tokens).
5. **Total Request Context**: ~2,150 tokens, keeping total inference cost under $0.0004 per consultation turn.

---

## 5. Architectural Assumptions & Constraints

- **[ASSUMPTION-AI-01]** Gemini 2.5 Flash serves as the primary inference engine via secure backend proxy; direct client-side API key embedding is prohibited.
- **[ASSUMPTION-AI-02]** On-device local SQLite FTS5 handles zero-connectivity fallback for rubric lookups, ensuring the doctor is never blocked during an internet outage.
