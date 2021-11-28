package ch.droduit.quickchat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class WebLayerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testOkStatus() throws Exception {
        mockMvc.perform(get("http://localhost/")).andDo(print()).andExpect(status().isOk());
        mockMvc.perform(get("http://localhost/admin")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void testRedirectionStatusWhenNotLoggedIn() throws Exception {
        mockMvc.perform(get("http://localhost/admin/dashboard")).andDo(print()).andExpect(status().is3xxRedirection());
    }

}
