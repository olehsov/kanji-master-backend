package com.kanjimaster;

import com.kanjimaster.ai.GrammarTaskGenerator;
import com.kanjimaster.ai.SubtitleGenerator;
import com.kanjimaster.ai.stub.StubGrammarTaskGenerator;
import com.kanjimaster.ai.stub.StubSubtitleGenerator;
import com.kanjimaster.repository.VideoMetadataRepository;
import com.kanjimaster.support.PostgresContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Whole application against a real migrated Postgres, with the default stub AI provider and no AI keys.
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureHttpGraphQlTester
@Import(PostgresContainer.class)
@Testcontainers(disabledWithoutDocker = true)
class KanjiMasterApplicationIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HttpGraphQlTester graphQlTester;
    @Autowired
    private GrammarTaskGenerator grammarTaskGenerator;
    @Autowired
    private SubtitleGenerator subtitleGenerator;
    @Autowired
    private VideoMetadataRepository videoMetadataRepository;

    @Test
    void startsInStubModeWithoutAiKeys() {
        assertThat(grammarTaskGenerator).isInstanceOf(StubGrammarTaskGenerator.class);
        assertThat(subtitleGenerator).isInstanceOf(StubSubtitleGenerator.class);
    }

    @Test
    void graphQlServesRealData() {
        graphQlTester.document("{ getKanji(kanji: \"日\") { kanji meaning jlpt } }")
                .execute()
                .path("getKanji.kanji").entity(String.class).isEqualTo("日");

        graphQlTester.document("{ getKanjis(page: 0, size: 5, filter: {jlpt: [\"N5 (beginner)\"]}) { content { kanji jlpt } totalElements } }")
                .execute()
                .path("getKanjis.content").entityList(Object.class).hasSize(5)
                .path("getKanjis.content[*].jlpt").entityList(String.class).satisfies(levels ->
                        assertThat(levels).containsOnly("N5 (beginner)"));

        graphQlTester.document("{ getRadicalSearchOptionDto { radical strokes isSimplifiedRadical } }")
                .execute()
                .path("getRadicalSearchOptionDto").entityList(Object.class).satisfies(list -> assertThat(list).isNotEmpty());
    }

    @Test
    void searchWithoutMatchesReturnsEmptyPage() {
        graphQlTester.document("{ getKanjis(filter: {search: \"zzzzzz\"}) { content { kanji } totalElements } }")
                .execute()
                .path("getKanjis.totalElements").entity(Integer.class).isEqualTo(0);
    }

    @Test
    void grammarTaskReturnsStubLesson() {
        graphQlTester.document("""
                        { getTopicTask(topicId: 1, settings: [{type: FILL_IN_THE_BLANKS, taskAmount: 3}]) {
                            header formation tasks { taskType } } }""")
                .execute()
                .path("getTopicTask.formation").entity(String.class).satisfies(f -> assertThat(f).isNotBlank())
                .path("getTopicTask.tasks").entityList(Object.class).hasSize(3);
    }

    @Test
    void subtitleEndpointReturnsStubAndDoesNotCacheIt() throws Exception {
        mockMvc.perform(post("/api/subtitle/extract-youtube-video").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"youtubeUrl\":\"https://www.youtube.com/watch?v=qal34e9v_pk\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subtitles.length()").value(3));

        assertThat(videoMetadataRepository.findByYoutubeId("qal34e9v_pk")).isEmpty();
    }

    @Test
    void pdfIsGeneratedFromDatabase() throws Exception {
        mockMvc.perform(post("/pdf/kanji-practice").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"search\":\"nichi\",\"grade\":[\"Kyōiku-Jōyō (1st grade of primary school)\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void apiDocumentationIsServed() throws Exception {
        mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/pdf/kanji-practice']").exists())
                .andExpect(jsonPath("$.paths['/api/subtitle/extract-youtube-video']").exists());
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
        mockMvc.perform(get("/graphiql")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/graphql/schema")).andExpect(status().isOk());
    }

    @Test
    void corsAllowsFrontendOrigin() throws Exception {
        mockMvc.perform(options("/graphql")
                        .header("Origin", "https://kanji-master-frontend-695666739311.europe-west1.run.app")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin",
                        "https://kanji-master-frontend-695666739311.europe-west1.run.app"));
    }
}
