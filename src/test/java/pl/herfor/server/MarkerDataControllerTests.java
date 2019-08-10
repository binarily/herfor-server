package pl.herfor.server;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.herfor.server.data.controllers.MarkerController;
import pl.herfor.server.data.objects.MarkerData;
import pl.herfor.server.data.repositories.MarkerRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class MarkerDataControllerTests {

    @Mock
    MarkerRepository repository;
    @InjectMocks
    MarkerController controller;

    @Test
    public void shouldListAllElements() {
        //given
        List<MarkerData> markersMockList = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            markersMockList.add(new MarkerData());
        when(repository.findAll()).thenReturn(markersMockList);
        //when
        List<MarkerData> results = controller.all();
        //then
        assertEquals(5, results.size());
    }

}
