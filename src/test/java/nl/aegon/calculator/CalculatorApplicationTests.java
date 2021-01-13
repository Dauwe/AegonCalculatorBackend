package nl.aegon.calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.aegon.calculator.model.Calculation;
import nl.aegon.calculator.model.Method;
import nl.aegon.calculator.repository.CalculationRepository;
import nl.aegon.calculator.web.dto.CalculationDto;
import nl.aegon.calculator.web.transformer.CalculationTransformer;
import org.assertj.core.util.Lists;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class CalculatorApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CalculationRepository repository;

    @Test
    void getCalculations() throws Exception {
        //given
        Calculation calculation = new Calculation();
        calculation.setNumberOne(2);
        calculation.setNumberTwo(3);
        calculation.setMethod(Method.MULTIPLY);
        calculation.setResult(6);
        repository.save(calculation);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                get("/calculator")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus(), CoreMatchers.is(200));
        assertThat(response.getContentAsString(), Is.is("[{\"id\":1,\"numberOne\":2,\"numberTwo\":3,\"method\":\"MULTIPLY\",\"result\":6.0}]"));
    }

    @Test
    void calculateMultiply() throws Exception {
        //given
        Calculation calculation = new Calculation();
        calculation.setNumberOne(2);
        calculation.setNumberTwo(3);
        calculation.setMethod(Method.MULTIPLY);
        calculation.setResult(6);

        //when
        final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
        MockHttpServletResponse response = mockMvc.perform(
                post("/calculator")
                        .content(objectMapper.writeValueAsString(CalculationTransformer.toDto(calculation)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus(), CoreMatchers.is(200));
        assertThat(response.getContentAsString(), Is.is("{\"id\":1,\"numberOne\":2,\"numberTwo\":3,\"method\":\"MULTIPLY\",\"result\":6.0}"));
    }

    @Test
    void calculateDivideByZeroShouldReturnBadRequestAndShouldNotSaveInDB() throws Exception {
        //given
        assertEquals(repository.count(), 0);
        CalculationDto calculationDto = new CalculationDto();
        calculationDto.setNumberOne(2);
        calculationDto.setNumberTwo(0);
        calculationDto.setMethod("DIVIDE");

        //when
        final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
        MockHttpServletResponse response = mockMvc.perform(
                post("/calculator")
                        .content(objectMapper.writeValueAsString(calculationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus(), CoreMatchers.is(400));
        assertThat(response.getContentAsString(), Is.is("Can't divide by zero."));
        assertEquals(repository.count(), 0);
    }

    @Test
    void calculateInvalidCalculationShouldReturnBadRequestAndShouldNotSaveInDB() throws Exception {
        //given
        assertEquals(repository.count(), 0);
        CalculationDto calculationDto = new CalculationDto();
        calculationDto.setNumberOne(2);

        //when
        final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
        MockHttpServletResponse response = mockMvc.perform(
                post("/calculator")
                        .content(objectMapper.writeValueAsString(calculationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus(), CoreMatchers.is(400));
        assertThat(response.getContentAsString(), Is.is("Both numbers and method of calculation are required."));
        assertEquals(repository.count(), 0);
    }
}
