package com.safe_route.safe.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;

import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@Import(RestDocConfiguration.class)
@AutoConfigureRestDocs
@SpringBootTest
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("PathFinder Test")
    void pathFinderTest() throws Exception {
        
        this.mockMvc.perform(get("/safe/routing?srcLati=35.242355&srcLongti=128.696837&dstLati=35.232539&dstLongti=128.674172&safeDegree=0"))
            .andDo(print())
            .andDo(document("pathfinder-get",
                requestParameters(
                    parameterWithName("srcLati").description("출발지 위도"),
                    parameterWithName("srcLongti").description("출발지 경도"),
                    parameterWithName("dstLati").description("목적지 위도"),
                    parameterWithName("dstLongti").description("목적지 경도"),
                    parameterWithName("safeDegree").description("안전도 0 ~ 1")
                ),
                responseFields( 
                    fieldWithPath("error").type(JsonFieldType.VARIES).description("오류 여부"),
                    fieldWithPath("total").type(JsonFieldType.NUMBER).description("총 경유지 개수"),
                    fieldWithPath("data.[].type").type(JsonFieldType.NUMBER).description("노드 유형 (1 ~ 5)"),
                    fieldWithPath("data.[].name").type(JsonFieldType.STRING).description("노드 설명"),
                    fieldWithPath("data.[].lati").type(JsonFieldType.NUMBER).description("경유지 노드 위도"),
                    fieldWithPath("data.[].longti").type(JsonFieldType.NUMBER).description("경유지 노드 경도"),
                    fieldWithPath("data.[].oriLati").type(JsonFieldType.VARIES).description("노드 원본 위도, 미사용 "),
                    fieldWithPath("data.[].oriLongti").type(JsonFieldType.VARIES).description("노드 원본 경도, 미사용")
                ))
            )
        .andExpect(jsonPath("data.[0].type").isNumber())
        .andExpect(jsonPath("data.[0].lati").isNumber())
        .andExpect(jsonPath("data.[0].longti").isNumber());
    }
}