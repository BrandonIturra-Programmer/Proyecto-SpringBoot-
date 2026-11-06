package ar.edu.unvime.api_blank.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ListaIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void accederAListaPropiaDevuelve200() throws Exception {
        registrar("dueno-propia@test.com");
        String token = login("dueno-propia@test.com");
        long listaId = crearLista(token, "Mis regalos");

        mockMvc.perform(get("/api/listas/" + listaId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mis regalos"));
    }

    @Test
    void accederAListaAjenaDevuelve403() throws Exception {
        registrar("dueno-ajena@test.com");
        registrar("intruso@test.com");
        String tokenDueno = login("dueno-ajena@test.com");
        String tokenIntruso = login("intruso@test.com");
        long listaId = crearLista(tokenDueno, "Privada");

        mockMvc.perform(get("/api/listas/" + listaId)
                        .header("Authorization", "Bearer " + tokenIntruso))
                .andExpect(status().isForbidden());
    }

    @Test
    void accederAListasSinTokenDevuelve401() throws Exception {
        mockMvc.perform(get("/api/listas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminAccediendoAListaAjenaDevuelve200() throws Exception {
        registrar("dueno-admin@test.com");
        registrar("admin@test.com");
        String tokenDueno = login("dueno-admin@test.com");
        long listaId = crearLista(tokenDueno, "De otro usuario");

        // El registro siempre crea USER: se promueve directo en la base y
        // se vuelve a loguear para que el token nuevo lleve el rol ADMIN.
        jdbcTemplate.update("UPDATE usuarios SET rol = 'ADMIN' WHERE email = ?", "admin@test.com");
        String tokenAdmin = login("admin@test.com");

        mockMvc.perform(get("/api/listas/" + listaId)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("De otro usuario"));
    }

    // ---- helpers ----

    private void registrar(String email) throws Exception {
        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"" + email + "\", \"password\": \"123456\"}"))
                .andExpect(status().isCreated());
    }

    private String login(String email) throws Exception {
        String body = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"" + email + "\", \"password\": \"123456\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.token");
    }

    private long crearLista(String token, String nombre) throws Exception {
        String body = mockMvc.perform(post("/api/listas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"" + nombre + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(body, "$.id")).longValue();
    }
}