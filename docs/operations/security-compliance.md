# Similimum AI — Security, DPDP Act 2023 & HIPAA Compliance
`Location: /docs/operations/security-compliance.md`

---

## 1. Regulatory Governance Framework

Similimum AI processes sensitive health data during homeopathic consultations. The application is architected to adhere strictly to:
1. **Digital Personal Data Protection Act (DPDP Act 2023, India)**:
   - The consulting physician acts as the **Data Fiduciary**.
   - The patient is the **Data Principal**.
   - Similimum AI serves as an offline-first **Data Processor / Local Clinical Tool**.
2. **National Commission for Homoeopathy (NCH) & Telemedicine Practice Guidelines (AYUSH, India)**:
   - Preservation of patient autonomy, informed consent, and mandatory record keeping.
3. **Health Insurance Portability and Accountability Act (HIPAA Security & Privacy Rules, US)**:
   - End-to-end encryption of Protected Health Information (PHI) at rest and in transit.

---

## 2. Privacy-by-Design Architectural Safeguards

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       PRIVACY-BY-DESIGN ARCHITECTURE                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  [Acoustic Audio Stream]                                                    │
│         │                                                                   │
│         ▼                                                                   │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │ 1. Zero-Retention Memory Buffer                                       │  │
│  │    - Raw PCM audio samples processed in-memory only                   │  │
│  │    - Audio samples discarded immediately after text extraction        │  │
│  │    - Zero audio files written to disk or uploaded to cloud            │  │
│  └──────────────────────────────────┬────────────────────────────────────┘  │
│                                     │                                       │
│                                     ▼                                       │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │ 2. Local PII Redaction & Storage                                      │  │
│  │    - Patient demographic data (Name, Phone, MRN) stored strictly      │  │
│  │      on-device in AES-256 encrypted SQLCipher SQLite DB               │  │
│  │    - Hardware KeyStore master key protection                          │  │
│  └──────────────────────────────────┬────────────────────────────────────┘  │
│                                     │                                       │
│                                     ▼                                       │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │ 3. De-Identified Cloud AI Inference (Gemini 2.5 Flash)                │  │
│  │    - Cloud requests contain ONLY anonymized clinical symptoms         │  │
│  │    - All names, ages, phone numbers, and addresses are stripped       │  │
│  │    - Transmitted over TLS 1.3 with Certificate Pinning                │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Data Protection Safeguards

### 3.1 Data at Rest (AES-256 SQLCipher & StrongBox)
- The local database (`consultation_database.db`) is encrypted using SQLCipher with 256-bit AES-GCM.
- Key generation utilizes `KeyGenParameterSpec` configured with `PURPOSE_ENCRYPT | PURPOSE_DECRYPT` inside `AndroidKeyStore`.
- On devices supporting Android 9.0+ with hardware StrongBox (e.g., Google Pixel Titan M2, Samsung Knox Vault), key material is isolated from the main application processor.

### 3.2 Data in Transit (TLS 1.3 & Network Security Config)
The network security configuration explicitly enforces modern cipher suites and TLS 1.3:
```xml
<!-- In res/xml/network_security_config.xml -->
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="false">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
    <domain-config>
        <domain includeSubdomains="true">generativelanguage.googleapis.com</domain>
        <pin-set expiration="2027-01-01">
            <!-- Google Trust Services Root Pin -->
            <pin digest="SHA-256">hxqRlPTuQjvW/wXMYrSc0RAVoMV563YpfOWxgZsssvQ=</pin>
        </pin-set>
    </domain-config>
</network-security-config>
```

---

## 4. Patient Consent & Statutory Rights

### 4.1 Real-Time Consulting Consent
Before the ambient microphone starts capturing dialogue, the user interface displays a prominent statutory disclaimer:
> *"Patient verbal consent has been obtained for AI-assisted clinical consultation note taking. Raw voice recordings are processed in memory and never stored or uploaded."*

### 4.2 Right to Erasure & Portability (DPDP Act Compliance)
- **Data Portability**: Clinicians can export a patient's complete case record in encrypted JSON or plain-text format at any time.
- **Right to Erasure (Forget Me)**: Tapping "Delete Patient Record" permanently cascades deletion across `PatientEntity`, `ConsultationSessionEntity`, `SymptomRecordEntity`, and `PrescriptionRecordEntity` with immediate zeroing of allocated disk blocks.

`[ASSUMPTION-OPS-06]` Zero raw audio recordings are written to device disk or uploaded to external servers; speech data is processed purely in memory and immediately discarded.
