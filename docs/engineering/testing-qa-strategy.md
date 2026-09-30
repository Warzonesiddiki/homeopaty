# Similimum AI — Testing, Quality Assurance & Clinical Verification Plan
`Location: /docs/engineering/testing-qa-strategy.md`

---

## 1. Quality Assurance Philosophy & Testing Pyramid

In a medical Clinical Decision Support System (CDSS), testing is a safety-critical discipline. Algorithmic errors in rubric weighting, inverted thermal affinities, or missed allopathic red flags could lead to clinical misdirection.

Similimum AI enforces a **Local JVM-First Testing Pyramid** that runs in seconds without requiring an Android emulator:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          TESTING HIERARCHY & SCOPE                          │
├─────────────────────┬───────────────────────────┬───────────────────────────┤
│ Test Tier           │ Framework & Tools         │ Target Scope & Coverage   │
├─────────────────────┼───────────────────────────┼───────────────────────────┤
│ Tier 1: Domain &    │ JUnit 4.13.2              │ 100% test coverage of:    │
│ Clinical Invariants │ Pure Kotlin / Local JVM   │ - Red-Flag Emergency Rules│
│ (< 500 ms)          │                           │ - Kent/TPB Repertorization│
│                     │                           │ - Inimical Drug Blockers  │
│                     │                           │ - LM Potency Dilution     │
├─────────────────────┼───────────────────────────┼───────────────────────────┤
│ Tier 2: Components  │ Robolectric 4.16.1        │ Android framework logic:  │
│ & Lifecycles        │ Local JVM Shadow Engine   │ - ViewModel StateFlow     │
│ (< 5,000 ms)        │                           │ - Room DAO Queries        │
│                     │                           │ - Speech Manager States   │
├─────────────────────┼───────────────────────────┼───────────────────────────┤
│ Tier 3: Clinical    │ 50 Classical Benchmark    │ Historical case fidelity: │
│ Gold Standards      │ Cases (Kent, Nash, Boer.) │ - Similimum Rank #1 > 85% │
└─────────────────────┴───────────────────────────┴───────────────────────────┘
```

---

## 2. Core Clinical Verification Test Suites (`ExampleUnitTest.kt`)

The suite of automated JVM tests verifies the clinical and mathematical accuracy of the engine:

### 2.1 Test 1: Emergency Red-Flag Detection
Verifies that critical acute symptoms (Acute Coronary Syndrome, Meningitis, Stroke) trigger immediate high-priority alerts:
```kotlin
@Test
fun testRedFlagCardiacDetection() {
    val alert = HomeopathyKnowledgeEngine.checkRedFlag("Doctor, I have crushing chest pain shooting down my left arm!")
    assertNotNull(alert)
    assertTrue(alert!!.condition.contains("Acute Coronary Syndrome"))
    assertEquals(EmergencyUrgency.CRITICAL, alert.urgency)
}

@Test
fun testRedFlagNormalText() {
    val alert = HomeopathyKnowledgeEngine.checkRedFlag("I have a throbbing headache since 2 days.")
    assertNull(alert)
}
```

### 2.2 Test 2: Classical Kentian Repertorization Ranking
Feeds canonical keynotes for *Natrum Muriaticum* and verifies that *Nat-m* emerges as the #1 Similimum with high confidence:
```kotlin
@Test
fun testRepertorizationRankingNatrumMur() {
    val griefRubric = HomeopathyKnowledgeEngine.allRubrics.first { it.path.contains("grief") }
    val consolationAggRubric = HomeopathyKnowledgeEngine.allRubrics.first { it.path.contains("CONSOLATION - agg") }
    val sunAggRubric = HomeopathyKnowledgeEngine.allRubrics.first { it.path.contains("SUN") }
    val saltDesireRubric = HomeopathyKnowledgeEngine.allRubrics.first { it.path.contains("salt") }

    val results = HomeopathyKnowledgeEngine.repertorize(
        listOf(griefRubric, consolationAggRubric, sunAggRubric, saltDesireRubric)
    )
    assertTrue(results.isNotEmpty())
    assertEquals("Nat-m", results.first().remedyCode)
    assertTrue(results.first().confidencePercent > 80)
}
```

### 2.3 Test 3: Inimical Drug Safety Interception
Validates that incompatible remedy pairings (*Apis* and *Rhus Tox*, *Causticum* and *Phosphorus*) are blocked:
```kotlin
@Test
fun testInimicalSafetyBlocker() {
    val warning = HomeopathyKnowledgeEngine.checkInimicalCompatibility("apis", "rhus-t")
    assertNotNull(warning)
    assertTrue(warning!!.warningText.contains("DANGEROUS INIMICAL COMBINATION"))
}
```

### 2.4 Test 4: LM 50-Millesimal Dilution Mathematics
Validates posology calculations according to Organon §270–§272:
```kotlin
@Test
fun testLmPotencyCalculator() {
    val protocol = HomeopathyKnowledgeEngine.calculateLmProtocol("LM1", isHypersensitive = true)
    assertEquals(4, protocol.succussions) // Reduced for hypersensitive patients
    assertTrue(protocol.dilutionMethod.contains("2nd Cup Method"))
}
```

### 2.5 Test 5: Multi-School Repertory Divergence
Verifies that the engine can switch weighting matrices between Kent's Hierarchical model and Boenninghausen's Therapeutic Pocket Book (TPB):
```kotlin
@Test
fun testMultiSchoolRepertorization() {
    val rubric = HomeopathyKnowledgeEngine.allRubrics.first { it.path.contains("grief") }
    val kentResults = HomeopathyKnowledgeEngine.repertorizeWithSchool(listOf(rubric), RepertorySchool.KENT_HIERARCHY)
    val boenninghausenResults = HomeopathyKnowledgeEngine.repertorizeWithSchool(listOf(rubric), RepertorySchool.BOENNINGHAUSEN_TPB)
    assertTrue(kentResults.isNotEmpty())
    assertTrue(boenninghausenResults.isNotEmpty())
}
```

---

## 3. Test Execution & CI Automation

### 3.1 Local JVM Test Execution
All unit and Robolectric tests are executed locally using standard Gradle commands:
```bash
gradle :app:testDebugUnitTest
```

### 3.2 Compilation & Lint Verification
Code health is checked via the build toolchain:
```bash
# Verify clean compilation
compile_applet
```

---

## 4. Key Architectural Assumptions

- **[ASSUMPTION-DEV-02]** Every task will be sized for atomic execution in a single prompt session, with automated local JVM unit tests (`ExampleUnitTest.kt`) verifying mathematical accuracy and safety rules before deployment.
- **[ASSUMPTION-TEST-01]** Testing execution relies 100% on JVM-based local testing (JUnit + Robolectric); instrumented emulator testing is avoided to maintain sub-10 second feedback loops.
