# Similimum AI — Internationalization (i18n) & Localization Plan
`Location: /docs/design/i18n-plan.md`

---

## 1. Localization Strategy & Phase 1 Target Languages

To serve the vast Indian AYUSH ecosystem, Similimum AI is engineered with full internationalization from day one:
- **Default Locale**: English (`en-IN` / `en-US`) — The universal language of classical repertories and medical records.
- **First Supported Vernacular Locale**: Hindi (`hi-IN`) — Spoken by over 500 million people across North and Central India.
- **Future Expansion Locales (v2.0+)**: Marathi (`mr-IN`), Bengali (`bn-IN`), Gujarati (`gu-IN`), Tamil (`ta-IN`).

---

## 2. Android Resource Architecture (`values` vs `values-hi`)

All UI strings, disclaimers, button labels, and clinical category descriptions are externalized into Android string resources:

```
app/src/main/res/
  ├── values/
  │    └── strings.xml      (English strings & canonical Latin remedy names)
  ├── values-hi/
  │    └── strings.xml      (Hindi Devnagari translated strings)
  └── values-b+hi+Latn/
       └── strings.xml      (Hinglish transliteration for bilingual clinics)
```

### Sample Key Mappings (`strings.xml` vs `values-hi/strings.xml`):

```xml
<!-- values/strings.xml (English) -->
<resources>
    <string name="app_name">Similimum AI</string>
    <string name="start_mic">Start Ambient Mic</string>
    <string name="pause_mic">Pause Mic</string>
    <string name="ask_next">High-Yield Questions</string>
    <string name="totality_ranking">Totality Ranking</string>
    <string name="repertory_school">Repertory School</string>
    <string name="disclaimer_cdss">Clinical Decision Support Only • Physician Validation Required</string>
    <string name="red_flag_alert">CRITICAL EMERGENCY RED FLAG</string>
    <string name="herings_law_title">Hering\'s Law of Cure Assessment</string>
</resources>

<!-- values-hi/strings.xml (Hindi) -->
<resources>
    <string name="app_name">सिमिलिमम एआई</string>
    <string name="start_mic">माइक चालू करें</string>
    <string name="pause_mic">माइक रोकें</string>
    <string name="ask_next">महत्वपूर्ण पूरक प्रश्न</string>
    <string name="totality_ranking">टोटैलिटी रैंकिंग</string>
    <string name="repertory_school">रेपर्टरी पद्धति</string>
    <string name="disclaimer_cdss">केवल चिकित्सीय निर्णय सहायता • चिकित्सक सत्यापन अनिवार्य</string>
    <string name="red_flag_alert">गंभीर आपातकालीन चेतावनी (रेड फ्लैग)</string>
    <string name="herings_law_title">हेरिंग्स लॉ ऑफ क्योर मूल्यांकन</string>
</resources>
```

---

## 3. Bilingual Vernacular Speech Recognition Configuration

The Android `SpeechRecognizer` is dynamically configured to accept both English and Hindi audio input:
```kotlin
val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN")
    putExtra(RecognizerIntent.EXTRA_ADDITIONAL_LANGUAGES, arrayOf("hi-IN"))
    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
}
```

`[ASSUMPTION-I18N-01]` Classical remedy Latin names (e.g. *Nux Vomica*, *Arsenicum Album*) remain in Latin across all locales to preserve global pharmacological standardization.
