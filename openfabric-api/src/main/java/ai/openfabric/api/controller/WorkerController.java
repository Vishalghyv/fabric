package ai.openfabric.api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import ai.openfabric.api.model.Worker;

@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController extends WorkerControllerBase {

    public Boolean fetchContainers() {
        try {
            List<Worker> containers = fetchAllWorkers();
            workerRepository.saveAll(containers);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @GetMapping("/")
    public @ResponseBody Page<Worker> getAllWorkersPaginated(@RequestParam(defaultValue = "1") int page) throws Exception{
        page--;

        if (fetchContainers()) {
            throw new Exception("Couldn't connect to docker");
        }

        Pageable sortedByState = PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by("state"));

        return workerRepository.findAll(sortedByState);
    }
}
