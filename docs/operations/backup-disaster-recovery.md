# Similimum AI — Backup, Data Retention & Disaster Recovery
`Location: /docs/operations/backup-disaster-recovery.md`

---

## 1. Disaster Recovery & Threat Modeling

Homeopathic clinical practices rely on longitudinal records spanning years, as constitutional prescribing requires reviewing earlier miasmatic layers, childhood suppressed eruptions, and historical potencies. Losing patient case records impairs long-term case management.

### Threat Model Matrix
| Threat Scenario | Likelihood | Impact | Architectural Mitigation |
|---|---|---|---|
| **Device Physical Loss / Theft** | Medium | Critical | SQLCipher AES-256 encryption prevents data extraction without master password. |
| **Android OS Crash / Bootloop** | Low | High | Periodic encrypted database exports saved to external SD card or doctor's PC. |
| **App Uninstallation / Clear Data** | Low | Critical | Warnings during setup; encrypted manual backup files stored in external documents folder. |
| **Database File Corruption** | Very Low | High | SQLite Write-Ahead Logging (WAL) and automatic integrity verification at startup. |

---

## 2. Android Auto-Backup & Cloud Sync Policy

### 2.1 Android Auto-Backup Exclusion
Under Indian DPDP Act 2023 and HIPAA guidelines, health records must not be silently uploaded to unencrypted personal Google Drive accounts via default Android Auto-Backup.

In `app/src/main/res/xml/backup_rules.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<full-backup-content>
    <!-- EXCLUDE all clinical databases and encrypted shared preferences -->
    <exclude domain="database" path="consultation_database.db" />
    <exclude domain="database" path="consultation_database.db-wal" />
    <exclude domain="database" path="consultation_database.db-shm" />
    <exclude domain="sharedpref" path="secure_clinical_prefs.xml" />
    
    <!-- INCLUDE only non-sensitive UI settings (Theme preference, font size) -->
    <include domain="sharedpref" path="user_preferences.xml" />
</full-backup-content>
```

---

## 3. Encrypted Manual Export & Import Architecture

Similimum AI provides a doctor-initiated, zero-cloud encrypted archive system using the Android Storage Access Framework (SAF).

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       ENCRYPTED BACKUP WORKFLOW (SAF)                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  [Doctor Requests Export]                                                   │
│          │                                                                  │
│          ▼                                                                  │
│  [Doctor inputs Master Backup Passphrase (min 12 chars)]                    │
│          │                                                                  │
│          ▼                                                                  │
│  [Key Derivation: Argon2id / PBKDF2 (100,000 iterations)]                   │
│          │                                                                  │
│          ▼                                                                  │
│  [SQLite Dump ──► JSON Serialization ──► AES-256-GCM Encryption]            │
│          │                                                                  │
│          ▼                                                                  │
│  [Saved as `similimum_backup_YYYYMMDD_HHMM.enc` to selected SAF directory]  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 3.1 Encryption Specifications
- **Cipher**: AES/GCM/NoPadding (256-bit key).
- **IV / Nonce**: 12-byte cryptographically secure random bytes generated per archive.
- **Authentication Tag**: 128-bit GCM tag for tamper detection.
- **Key Derivation**: PBKDF2WithHmacSHA256 with 100,000 iterations and 16-byte random salt.

---

## 4. Recovery Objectives (RTO & RPO)

- **Recovery Point Objective (RPO)**:
  - Completed Consultations: **RPO = 0**. Every dispensed prescription and saved symptom set commits immediately via atomic SQLite transactions.
  - Active In-Consultation Transcript: Max 5 seconds of uncommitted buffer.
- **Recovery Time Objective (RTO)**:
  - Full Database Restore from Encrypted Backup File: **< 15 seconds** for 10,000 patient records.
  - App Reinstallation & Key Regeneration: **< 3 minutes**.

---

## 5. Automated Database Integrity Checks

During application startup, `ConsultationDatabase` executes a non-blocking diagnostic routine:
```sql
PRAGMA quick_check;
PRAGMA foreign_key_check;
```
If corruption is detected, the database transitions to Read-Only Emergency Recovery Mode and prompts the practitioner to restore from the most recent verified backup file.

`[ASSUMPTION-OPS-04]` Patient case databases are excluded from unencrypted Android Auto-Backup; doctor-controlled encrypted export/import via Storage Access Framework ensures DPDP Act compliance.
