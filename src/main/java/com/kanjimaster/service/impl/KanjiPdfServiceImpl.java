package com.kanjimaster.service.impl;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.service.KanjiPdfService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class KanjiPdfServiceImpl implements KanjiPdfService {
    private final int NUM_COLUMN = 10;

    @Override
    public byte[] buildKanjiLearnFile(Map<KanjiInfo, String> kanjiesMap) throws IOException {
        List<KanjiInfo> kanjies = kanjiesMap.keySet().stream().sorted(Comparator.comparing(KanjiInfo::getFrequency)).toList();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        FontProgram fontProgram = FontProgramFactory.createFont("HeiseiKakuGo-W5");
        PdfFont font = PdfFontFactory.createFont(fontProgram, "UniJIS-UCS2-H");

        document.setFont(font);
        kanjies.forEach(kanjiInfo -> updateDocumentWithKanji(document, pdf, kanjiInfo, kanjiesMap.get(kanjiInfo)));

        document.close();

        return baos.toByteArray();
    }

    private void updateDocumentWithKanji(Document document, PdfDocument pdf, KanjiInfo kanjiInfo, String wordRep) {
        document.add(new Paragraph(kanjiInfo.getKanji())
                .setFontSize(60)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Meanings: " + kanjiInfo.getMeaning().replace(";", ", ")).setFontSize(14));
        document.add(new Paragraph("Onyomi: " + kanjiInfo.getOnyomi()).setFontSize(14));
        document.add(new Paragraph("Kunyomi: " + kanjiInfo.getKunyomi()).setFontSize(14));

        document.add(new Paragraph("Words: " + wordRep).setFontSize(14));


        document.add(buildTable(pdf, kanjiInfo));

        document.add(new Paragraph("\n"));
    }

    private Table buildTable(PdfDocument pdf, KanjiInfo kanjiInfo) {
        Table table = new Table(NUM_COLUMN);
        table.setKeepTogether(true);
        for (int i = 0; i < NUM_COLUMN * 3; i++) {
            table.addCell(buildCell(pdf, kanjiInfo, i));
        }

        return table;
    }

    private Cell buildCell(PdfDocument pdf, KanjiInfo kanjiInfo, int index) {
        final float cellWidth = pdf.getDefaultPageSize().getWidth() / NUM_COLUMN;

        float CELL_HEIGHT = 50;
        Cell cell = new Cell()
                .setHeight(CELL_HEIGHT)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setWidth(cellWidth)
                .setBorder(new SolidBorder(1));

        if (index % NUM_COLUMN == 0) {
            cell.add(new Paragraph(kanjiInfo.getKanji()).setFontSize(21));
        }

        updateCellRenderer(cell);

        return cell;
    }

    private void updateCellRenderer(Cell cell) {
        cell.setNextRenderer(new CellRenderer(cell) {
            @Override
            public void draw(DrawContext drawContext) {
                super.draw(drawContext);
                Rectangle rect = getOccupiedAreaBBox();
                PdfCanvas canvas = drawContext.getCanvas();

                canvas.saveState();

                PdfExtGState gState = new PdfExtGState();
                gState.setFillOpacity(0.5f);
                gState.setStrokeOpacity(0.5f);
                canvas.setExtGState(gState);

                canvas.setLineWidth(0.5f);
                canvas.setStrokeColor(ColorConstants.LIGHT_GRAY);


                // Calculate center points
                float centerX = rect.getLeft() + rect.getWidth() / 2;
                float centerY = rect.getBottom() + rect.getHeight() / 2;

                // Draw vertical line
                canvas.moveTo(centerX, rect.getBottom());
                canvas.lineTo(centerX, rect.getTop());

                // Draw horizontal line
                canvas.moveTo(rect.getLeft(), centerY);
                canvas.lineTo(rect.getRight(), centerY);

                canvas.stroke();
                canvas.restoreState();
            }
        });
    }
}
