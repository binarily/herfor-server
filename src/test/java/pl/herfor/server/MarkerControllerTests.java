package pl.herfor.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.herfor.server.data.controllers.MarkerController;
import pl.herfor.server.data.objects.Marker;
import pl.herfor.server.data.repositories.MarkerRepository;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class MarkerControllerTests {

    @Mock
    MarkerRepository repository;
    @InjectMocks
    MarkerController controller;
    //List used to mock data provided by repository
    List<Marker> markersMockList;

    @Test
    public void shouldListAllElements() {
        //given
        markersMockList = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            markersMockList.add(new Marker());
        when(repository.findAll()).thenReturn(markersMockList);
        //when
        List<Marker> results = controller.all();
        //then
        assertEquals(5, results.size());
    }

}
