package nz.org.vincenzo.betadog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * The integration test case for {@link BetaDogApplication} if the context loads.
 *
 * @author Rey Vincent Babilonia
 */
@SpringBootTest
@ActiveProfiles("test")
class BetaDogApplicationTest {

    @Test
    public void contextLoads() {
    }
}
