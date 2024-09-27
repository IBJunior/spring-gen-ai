package fly.intelligent.genai.retrieval;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorStoreInitializationRunner implements ApplicationRunner {
    private final VectorStore vectorStore;
    private @Value("classpath:documents/fly-intelligent-faq.pdf") String faq;
    private @Value("classpath:documents/intro-fly-intelligent.pdf") String mission;

    public VectorStoreInitializationRunner(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<Document> faqDocuments = this.transformWithTokenTextSplitter(
                this.loadParagraphsFromPDFDocument(this.faq)
        );
        List<Document> missionDocuments = this.transformWithTokenTextSplitter(
                this.loadParagraphsFromPDFDocument(this.mission)
        );
        this.vectorStore.accept(faqDocuments);
        this.vectorStore.accept(missionDocuments);

    }

    public List<Document> loadParagraphsFromPDFDocument(String resourceURL) {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resourceURL,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfBottomTextLinesToDelete(0)
                                .withNumberOfTopPagesToSkipBeforeDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        return pdfReader.get();
    }

    public List<Document> transformWithTokenTextSplitter(List<Document> documents) {
        var tokenTextSplitter = new TokenTextSplitter();
        return tokenTextSplitter.apply(documents);
    }
}
