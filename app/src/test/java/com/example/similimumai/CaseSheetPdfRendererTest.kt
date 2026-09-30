package com.example.similimumai

import com.example.similimumai.data.engine.CaseSheetPdfRenderer
import com.example.similimumai.data.engine.HomeopathyKnowledgeEngine
import com.example.similimumai.data.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tier 1: PDF Case Record renderer invariants
 * (docs/05 Phase 4: "Exportable PDF / Print-Ready Case Record").
 *
 * Includes a strict cross-reference (xref) table validation: the startxref
 * offset and every per-object offset must point exactly at the object header,
 * which catches any byte-layout regression in the renderer.
 */
class CaseSheetPdfRendererTest {

    @Test
    fun `pdf - valid header trailer and catalog structure`() {
        val pdf = String(CaseSheetPdfRenderer.renderPdf("SIMILIMUM AI\nhello world"), Charsets.US_ASCII)
        assertTrue(pdf.startsWith("%PDF-1.4\n"))
        assertTrue(pdf.endsWith("%%EOF\n"))
        assertTrue(pdf.contains("/Type /Catalog"))
        assertTrue(pdf.contains("/Type /Pages"))
        assertTrue(pdf.contains("/BaseFont /Helvetica"))
        assertTrue(pdf.contains("/BaseFont /Helvetica-Bold"))
    }

    @Test
    fun `pdf - xref table offsets point at the correct object headers`() {
        val bytes = CaseSheetPdfRenderer.renderPdf("line one\nline two\nline three")
        val pdf = String(bytes, Charsets.US_ASCII)

        // startxref must point at "xref"
        val startxref = pdf.substringAfterLast("startxref\n").substringBefore("\n").toInt()
        assertEquals("xref", pdf.substring(startxref, startxref + 4))

        // parse xref entries (20 bytes each) and validate each object offset
        val xrefBody = pdf.substring(startxref)
        val entryCount = xrefBody.substringAfter("\n").substringBefore("\n").split(" ").last().toInt()
        val entryStart = xrefBody.indexOf("0000000000")
        for (i in 1 until entryCount) {
            val entry = xrefBody.substring(entryStart + i * 20, entryStart + i * 20 + 20)
            assertTrue("entry $i must be type n", entry.endsWith(" n \n"))
            val offset = entry.substring(0, 10).toInt()
            val header = pdf.substring(offset, offset + ("$i 0 obj").length)
            assertEquals("offset for object $i", "$i 0 obj", header)
        }
    }

    @Test
    fun `pdf - page count matches content volume`() {
        // 200 short lines at 61 lines/page -> 4 pages
        val manyLines = (1..200).joinToString("\n") { "symptom line $it" }
        val pdf = String(CaseSheetPdfRenderer.renderPdf(manyLines), Charsets.US_ASCII)
        val pageCount = Regex("/Type /Page /Parent").findAll(pdf).count()
        assertEquals(4, pageCount)
        assertTrue(pdf.contains("/Count 4"))
    }

    @Test
    fun `pdf - single short document is one page`() {
        val pdf = String(CaseSheetPdfRenderer.renderPdf("just one line"), Charsets.US_ASCII)
        assertEquals(1, Regex("/Type /Page /Parent").findAll(pdf).count())
    }

    @Test
    fun `pdf - parentheses and backslashes are escaped in the content stream`() {
        val pdf = String(CaseSheetPdfRenderer.renderPdf("a(b)c\\d(e)"), Charsets.US_ASCII)
        assertTrue(pdf.contains("a\\(b\\)c\\\\d\\(e\\)"))
    }

    @Test
    fun `wrap - long lines break at word boundaries within the limit`() {
        val words = (1..40).joinToString(" ") { "word$it" }
        val wrapped = CaseSheetPdfRenderer.wrapLine(words, 86)
        assertTrue(wrapped.size > 1)
        wrapped.forEach { assertTrue(it.length <= 86) }
        assertEquals(words.replace(" ", ""), wrapped.joinToString("").replace(" ", ""))
    }

    @Test
    fun `wrap - overlong token without spaces is hard-broken`() {
        val token = "x".repeat(200)
        val wrapped = CaseSheetPdfRenderer.wrapLine(token, 86)
        wrapped.forEach { assertTrue(it.length <= 86) }
        assertEquals(token, wrapped.joinToString(""))
    }

    @Test
    fun `wrap - short line passes through unchanged`() {
        assertEquals(listOf("short line"), CaseSheetPdfRenderer.wrapLine("short line", 86))
    }

    @Test
    fun `transliterate - box-drawing and section glyphs map to ASCII`() {
        assertEquals("= S -", CaseSheetPdfRenderer.transliterate("═ § —"))
        assertEquals("plain ascii kept", CaseSheetPdfRenderer.transliterate("plain ascii kept"))
        // unknown high-bit character degrades to '?' rather than corrupting the stream
        assertEquals("a?b", CaseSheetPdfRenderer.transliterate("a\u0442b"))
    }

    @Test
    fun `pdf - full clinical case sheet renders a valid multi-section document`() {
        val rubrics = HomeopathyKnowledgeEngine.allRubrics
            .filter { it.id in setOf("r_mind_grief", "r_head_sun_agg", "r_stomach_salt_craving") }
        val scores = HomeopathyKnowledgeEngine.repertorize(rubrics)
        val sheet = HomeopathyKnowledgeEngine.generateCaseSheet(
            patientName = "Priya Sharma",
            patientAge = 32,
            patientSex = "Female",
            thermal = ThermalState.HOT,
            miasm = Miasm.PSORA,
            chiefComplaint = "Chronic migraine with silent grief",
            caseMode = CaseMode.CHRONIC.label,
            symptoms = listOf(
                Symptom(id = "s1", location = "Head (Cephalic)", sensation = "Throbbing",
                    modalities = "Aggravated by sun", isPqrs = true, intensity = 3)
            ),
            activeRubrics = rubrics,
            remedyScores = scores,
            rxRemedy = "Nat-m",
            rxPotency = "200C",
            rxScale = "Centesimal",
            rxPosology = "Single dose on tongue",
            dietaryRestrictions = listOf("Raw Onion", "Garlic")
        )

        val pdf = String(CaseSheetPdfRenderer.renderPdf(sheet), Charsets.US_ASCII)
        assertTrue(pdf.startsWith("%PDF-1.4\n"))
        assertTrue(pdf.contains("SIMILIMUM AI"))
        assertTrue(pdf.contains("Priya Sharma"))
        assertTrue(pdf.contains("PRESCRIPTION"))
        assertTrue(pdf.contains("Nat-m"))
        // xref is consistent for the full document
        val startxref = pdf.substringAfterLast("startxref\n").substringBefore("\n").toInt()
        assertEquals("xref", pdf.substring(startxref, startxref + 4))
    }
}
