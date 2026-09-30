# Similimum AI — Cost Model, Infrastructure & Resource Sizing
`Location: /docs/operations/cost-infrastructure-estimate.md`

---

## 1. Cost & Infrastructure Architecture

Similimum AI leverages a **Client-Heavy, Serverless-First Architecture**. By executing the speech recognition, Boenninghausen LSMC parsing, Kentian repertorization, and database persistence directly on the doctor's Android hardware, centralized infrastructure costs are reduced to near-zero.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          INFRASTRUCTURE COST MODEL                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  [Doctor's Android Device] ──── (100% On-Device Processing) ────► ₹0 / mo   │
│  - SpeechRecognizer (On-device neural model)                                │
│  - SQLite / SQLCipher (Local storage)                                       │
│  - Classical Knowledge Engine (30+ remedies, 150+ rubrics)                  │
│                                                                             │
│  [Gemini 2.5 Flash Cloud AI] ─── (Augmented Reasoning Only) ───► ₹4.50 / mo │
│  - Input: 1,600 tokens per consultation                                     │
│  - Output: 400 tokens per consultation                                      │
│  - Average 150 consultations / doctor / month                               │
│                                                                             │
│  [Doctor Subscription Revenue] ────────────────────────────────► ₹799 / mo  │
│  - Net Google Play Payout (85% after 15% tier fee): ₹679.15                 │
│  - Net Gross Margin: > 98.5%                                                │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Gemini 2.5 Flash Token & Inference Economics

Cloud AI inference is invoked selectively for complex constitutional analysis, miasmatic deconvolution, and comparative Materia Medica synthesis:

### 2.1 Per-Consultation Token Consumption Breakdown
- **System Instructions & Clinical Prompts**: ~1,200 tokens
- **Extracted Symptoms, LSMC Modalities & Generals**: ~400 tokens
- **Total Input Tokens**: **1,600 tokens**
- **JSON Schema Output (Remedies, Differentials, Prognosis)**: **400 tokens**

### 2.2 Google Cloud Pricing Reference (Gemini 2.5 Flash)
- **Input Pricing**: $0.075 per 1,000,000 tokens ($0.000075 / 1k tokens)
- **Output Pricing**: $0.30 per 1,000,000 tokens ($0.00030 / 1k tokens)

### 2.3 Unit Cost per Consultation
$$\text{Cost}_{\text{input}} = \frac{1,600}{1,000,000} \times \$0.075 = \$0.00012$$
$$\text{Cost}_{\text{output}} = \frac{400}{1,000,000} \times \$0.30 = \$0.00012$$
$$\text{Total Cost per Consultation} = \$0.00024 \text{ USD} \approx ₹0.020 \text{ INR}$$

Even if a busy clinical practitioner performs 250 AI-augmented consultations per month:
$$\text{Monthly Inference Cost per Doctor} = 250 \times \$0.00024 = \$0.060 \text{ USD} \approx ₹5.00 \text{ INR}$$

---

## 3. Tiered Infrastructure Scaling Budget (Monthly)

| Service Component | 100 Doctors (Pilot) | 1,000 Doctors (Growth) | 10,000 Doctors (Scale) |
|---|---|---|---|
| **Google Play Developer Account** | $25 (one-time) | — | — |
| **Gemini 2.5 Flash Inference** | $6.00 / mo | $60.00 / mo | $600.00 / mo |
| **Cloud Run API Proxy (v2+)** | $0.00 (Free Tier) | $15.00 / mo | $85.00 / mo |
| **Firebase Crashlytics / Analytics** | $0.00 (Free Tier) | $0.00 (Free Tier) | $0.00 (Free Tier) |
| **GitHub Actions CI/CD** | $0.00 (Included) | $0.00 (Included) | $20.00 / mo |
| **Total Monthly Infrastructure Cost**| **$6.00 (~₹500)** | **$75.00 (~₹6,250)** | **$705.00 (~₹58,700)** |
| **Monthly Subscription Revenue (₹799/doc)**| **₹79,900** | **₹799,000** | **₹7,990,000** |
| **Gross Margin Percentage** | **98.7%** | **98.6%** | **98.5%** |

---

## 4. Resource Sizing on Client Devices

To ensure fluid execution on affordable Android devices (e.g., Redmi, Samsung Galaxy A-series with 4GB–6GB RAM):
- **APK / AAB Size**: < 12 MB download; < 35 MB uncompressed.
- **RAM Working Set**: 85 MB baseline; 140 MB peak during speech recognition.
- **Battery Consumption**: < 4% battery drain per 60-minute active consulting session.
- **Storage Footprint**: ~50 KB per patient consultation record. A database with 2,000 patients consumes less than 100 MB of local flash storage.

`[ASSUMPTION-OPS-07]` On-device processing of speech recognition and repertorization keeps backend cloud costs below ₹5.00 per active doctor per month, enabling an exceptionally high gross margin (>98%).
