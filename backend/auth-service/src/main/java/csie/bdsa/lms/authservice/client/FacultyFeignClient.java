package csie.bdsa.lms.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("faculty-service")
public interface FacultyFeignClient {

    @GetMapping("/administrators/user-id/{id}/id")
    Long getAdministratorByUserId(@PathVariable Long id);

    @GetMapping("/teachers/user-id/{id}/id")
    Long getTeacherByUserId(@PathVariable Long id);

    @GetMapping("/students/user-id/{id}/id")
    Long getStudentIdByUserId(@PathVariable Long id);

}
