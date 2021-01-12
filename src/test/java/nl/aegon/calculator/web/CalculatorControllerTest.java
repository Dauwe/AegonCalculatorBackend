package nl.aegon.calculator.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.aegon.calculator.model.Calculation;
import nl.aegon.calculator.model.Method;
import nl.aegon.calculator.service.SimpleCalculator;
import nl.aegon.calculator.web.transformer.CalculationTransformer;
import org.assertj.core.util.Lists;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(value = CalculatorController.class)
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimpleCalculator simpleCalculatorMock;

    @Test
    void getCalculations() throws Exception {
        //given
        Calculation calculation = new Calculation();
        calculation.setNumberOne(2);
        calculation.setNumberTwo(3);
        calculation.setMethod(Method.MULTIPLY);
        calculation.setResult(6);
        given(simpleCalculatorMock.getCalculations()).willReturn(Lists.newArrayList(calculation, calculation));

        //when
        MockHttpServletResponse response = mockMvc.perform(
                get("/calculator")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus(), CoreMatchers.is(200));
        assertThat(response.getContentAsString(), Is.is("[{\"id\":0,\"numberOne\":2,\"numberTwo\":3,\"method\":\"MULTIPLY\",\"result\":6.0}," +
                "{\"id\":0,\"numberOne\":2,\"numberTwo\":3,\"method\":\"MULTIPLY\",\"result\":6.0}]"));
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
        when(simpleCalculatorMock.calculate(any(Calculation.class))).thenReturn(calculation);
        final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
        MockHttpServletResponse response = mockMvc.perform(
                post("/calculator")
                        .content(objectMapper.writeValueAsString(CalculationTransformer.toDto(calculation)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus(), CoreMatchers.is(200));
        assertThat(response.getContentAsString(), Is.is("{\"id\":0,\"numberOne\":2,\"numberTwo\":3,\"method\":\"MULTIPLY\",\"result\":6.0}"));
    }
}
