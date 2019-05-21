package com.tpbank.writeToPdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class WritePdfTest {

    private LinkedList<String> logLinkedList;
    private String nameFile;

    public WritePdfTest(LinkedList<String> logLinkedList, String nameFile) {
        this.logLinkedList = logLinkedList;
        this.nameFile = nameFile;
    }

    public void createTextToAPdf(LinkedList<String> logLinkedList, String nameFile) {

        PDDocument pdDocument = new PDDocument();

        PDPage pdPage = new PDPage();
        pdDocument.addPage(pdPage);
        String content = converteLinkedListToString(logLinkedList);

        // Define all the constants like X, Y coordinates, font, font size and
        // the pdf margin
        int cursorX = 25;
        int cursorY = 700;
        PDFont pdFont = PDType1Font.HELVETICA;
        int fontSize = 12;
        int margin = 50;

        // Using the PDRectangle class get the printableWidth
        PDRectangle mediabox = pdPage.getMediaBox();
        float printableWidth = mediabox.getWidth() - (2 * margin) - cursorX;

        // We will convert the String content into a array of strings/lines
        List<String> finalLines = new ArrayList<>();

        try {

            // Inside a While loop, check the length of the content Vs the
            // Printable width. If content length is greater, then cut the
            // length into the printable width allowed. Add that to the array.
            // Process the remaining content again in loop.
            boolean convertContent = true;
            while (convertContent) {
                int contentSize = (int) (fontSize * pdFont.getStringWidth(content) / 1000);
                if (contentSize > printableWidth) {

                    int contentLength = content.length();
                    float rem = contentSize / printableWidth;
                    float lenForSubString = contentLength / rem;
                    int lenForSubStringInt = (int) Math.floor(lenForSubString);
                    finalLines.add(content.substring(0, lenForSubStringInt));
                    content = content.substring(lenForSubStringInt);
                } else {
                    finalLines.add(content);
                    convertContent = false;
                }
            }

            PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, pdPage);
            pdPageContentStream.beginText();
            pdPageContentStream.newLineAtOffset(cursorX, cursorY);
            pdPageContentStream.setFont(pdFont, fontSize);

            // Setting a leading while avoid the overlapping of text.
            pdPageContentStream.setLeading(15f);


            Iterator<String> it = logLinkedList.iterator();
            int count = 0;
            while (it.hasNext()) {
                String value = it.next();
                pdPageContentStream.showText(value);
                pdPageContentStream.newLine();

            }
            pdPageContentStream.endText();
            pdPageContentStream.close();

            pdDocument.save("/home/thanhdinh/IdeaProjects/JavaSwing/PDFBox/" + nameFile + ".pdf");
            pdDocument.close();
            System.out.println("PDF saved to the location !!!");

        } catch (IOException ioe) {
            System.out.println("Error while saving pdf" + ioe.getMessage());
        }

    }

    private static String converteLinkedListToString(LinkedList<String> list) {

        String listString = "";

        StringBuilder string = new StringBuilder();
        Iterator<?> it = list.descendingIterator();

        while (it.hasNext()) {
            string.append(it.next());
            string.append(".^p");
        }
        listString = string.toString();
        return listString;
    }


}