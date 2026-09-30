package com.example.similimumai.data.engine

/**
 * Dependency-free PDF 1.4 renderer for the plain-text clinical Case Sheet
 * (docs/05 Phase 4: "Exportable PDF / Print-Ready Case Record").
 *
 * Emits a text-only document using the built-in Helvetica Type1 base fonts
 * (no embedded font files required). Pure Kotlin — no Android imports — so the
 * byte layout is fully verifiable in local JVM unit tests.
 */
object CaseSheetPdfRenderer {

    // A4 portrait, points
    const val PAGE_WIDTH = 595
    const val PAGE_HEIGHT = 842
    private const val MARGIN = 50
    private const val TOP_Y = 792
    private const val BOTTOM_Y = 50
    private const val BODY_SIZE = 10
    private const val HEADER_SIZE = 11
    private const val LINE_LEADING = 12
    private const val MAX_CHARS = 86
    private const val MAX_LINES_PER_PAGE = ((TOP_Y - BOTTOM_Y) / LINE_LEADING).toInt()

    /** Render the full case-sheet text into a valid PDF 1.4 document. */
    fun renderPdf(sheetText: String): ByteArray {
        val lines = sheetText.lineSequence().map { transliterate(it) }.toList()
        val wrapped = lines.flatMap { wrapLine(it, MAX_CHARS) }
        val pages = wrapped.chunked(MAX_LINES_PER_PAGE)
        val n = pages.size

        // Object map:
        //   1 Catalog | 2 Pages | 3 Font F1 | 4 Font F2
        //   5..(4+n) Pages, (5+n)..(4+2n) their content streams
        val totalObjects = 4 + 2 * n
        val pageObjs = (0 until n).associate { it to 5 + it }
        val contentObjs = (0 until n).associate { it to 5 + n + it }

        val out = StringBuilder()
        val offsets = ArrayList<Int>(totalObjects)

        fun addObject(number: Int, body: String) {
            check(number == offsets.size + 1) { "objects must be added in order" }
            offsets.add(out.length)
            out.append(number).append(" 0 obj\n").append(body).append("\nendobj\n")
        }

        // 1: Catalog
        addObject(1, "<< /Type /Catalog /Pages 2 0 R >>")
        // 2: Pages
        val kids = pageObjs.values.joinToString(", ") { "$it 0 R" }
        addObject(2, "<< /Type /Pages /Kids [$kids] /Count $n >>")
        // 3/4: base-14 fonts
        addObject(3, "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>")
        addObject(4, "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold /Encoding /WinAnsiEncoding >>")

        // 5..(4+n): page objects
        for (i in 0 until n) {
            addObject(
                pageObjs.getValue(i),
                "<< /Type /Page /Parent 2 0 R " +
                        "/MediaBox [0 0 $PAGE_WIDTH $PAGE_HEIGHT] " +
                        "/Resources << /Font << /F1 3 0 R /F2 4 0 R >> >> " +
                        "/Contents ${contentObjs.getValue(i)} 0 R >>"
            )
        }

        // (5+n)..(4+2n): content streams
        for (i in 0 until n) {
            val stream = buildContentStream(pages[i])
            addObject(
                contentObjs.getValue(i),
                "<< /Length ${stream.length} >>\nstream\n$stream\nendstream"
            )
        }

        // xref table (each entry exactly 20 bytes)
        val xrefOffset = out.length
        out.append("xref\n0 ").append(totalObjects).append("\n")
        out.append("0000000000 65535 f \n")
        for (offset in offsets) {
            out.append(offset.toString().padStart(10, '0')).append(" 00000 n \n")
        }
        out.append("trailer\n<< /Size $totalObjects /Root 1 0 R >>\n")
        out.append("startxref\n").append(xrefOffset).append("\n%%EOF\n")

        return out.toString().toByteArray(Charsets.US_ASCII)
    }

    private fun buildContentStream(pageLines: List<String>): StringBuilder {
        val sb = StringBuilder()
        sb.append("BT\n").append("/F1 ").append(BODY_SIZE).append(" Tf\n")
        sb.append(LINE_LEADING).append(" TL\n").append(MARGIN).append(" ").append(TOP_Y).append(" Td\n")
        for (line in pageLines) {
            val escaped = escapePdfString(line)
            if (isHeaderLine(line)) {
                sb.append("/F2 ").append(HEADER_SIZE).append(" Tf\n")
                sb.append("($escaped) Tj\nT*\n")
                sb.append("/F1 ").append(BODY_SIZE).append(" Tf\n")
            } else {
                sb.append("($escaped) Tj\nT*\n")
            }
        }
        sb.append("ET")
        return sb
    }

    private fun isHeaderLine(line: String): Boolean =
        line.startsWith("SIMILIMUM AI") || line.startsWith("-- ") || line.startsWith("──")

    /** Wrap at word boundaries where possible; hard-breaks overlong tokens. */
    fun wrapLine(line: String, maxChars: Int): List<String> {
        if (line.length <= maxChars) return listOf(line)
        val parts = mutableListOf<String>()
        var start = 0
        while (start < line.length) {
            var end = minOf(start + maxChars, line.length)
            if (end < line.length) {
                val spaceIdx = line.lastIndexOf(' ', end)
                if (spaceIdx > start) end = spaceIdx
            }
            parts.add(line.substring(start, end))
            start = end
            while (start < line.length && line[start] == ' ') start++
        }
        return parts
    }

    /** PDF strings here are WinAnsi/ASCII; transliterate the sheet's box-drawing glyphs. */
    fun transliterate(text: String): String {
        val map = mapOf(
            '═' to '=', '─' to '-', '│' to '|', '┌' to '+', '└' to '+', '┐' to '+', '┘' to '+',
            '§' to 'S', '—' to '-', '–' to '-', 'é' to 'e', 'è' to 'e', 'ê' to 'e',
            '’' to "'", '‘' to "'", '“' to '"', '”' to '"', '…' to "...", '→' to "->", '·' to '-'
        )
        return buildString {
            for (ch in text) {
                if (ch < 128) append(ch) else append(map[ch] ?: "?")
            }
        }
    }

    fun escapePdfString(text: String): String =
        text.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)")
}
