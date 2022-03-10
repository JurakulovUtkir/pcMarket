package uz.pdp.pcmarket.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.pcmarket.entity.Attachment;
import uz.pdp.pcmarket.entity.AttachmentContent;
import uz.pdp.pcmarket.repository.AttachmentContentRepository;
import uz.pdp.pcmarket.repository.AttachmentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;


@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    final AttachmentContentRepository attachmentContentRepository;
    final AttachmentRepository attachmentRepository;


    @GetMapping
    public HttpEntity<?> all() {
        return ResponseEntity.ok(attachmentRepository.findAll());
    }

    @GetMapping("/{id}")
    public HttpEntity<?> one(@PathVariable Integer id) {
        return ResponseEntity.ok(attachmentRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        attachmentRepository.deleteById(id);
        return ResponseEntity.ok("Mahsulot o'chirildi\n" + attachmentRepository.findAll());
    }

    /**
     *  uploading to the database
     * @param request
     * @return
     */
    @SneakyThrows
    @PostMapping("/upload")
    public HttpEntity<?> upload( MultipartHttpServletRequest request) {

        Iterator<String> fileNames = request.getFileNames();
        if (fileNames.hasNext()) {
            MultipartFile file = request.getFile(fileNames.next());

            Attachment attachment = new Attachment();

            attachment.setSize(file.getSize());
            attachment.setContentType(file.getContentType());
            attachment.setOriginalFileName(file.getOriginalFilename());

            Attachment save = attachmentRepository.save(attachment);

            AttachmentContent attachmentContent = new AttachmentContent();

            attachmentContent.setAttachment(save);
            attachmentContent.setBytes(file.getBytes());

            AttachmentContent save1 = attachmentContentRepository.save(attachmentContentRepository.save(attachmentContent));

            return ResponseEntity.ok("successfully done!");
        }
        return ResponseEntity.ok("already exists");
    }


    @GetMapping("/download/{id}")
    public HttpEntity<?> getFile(@PathVariable Integer id,
                        HttpServletResponse response) throws IOException {

        Optional<Attachment> byId = attachmentRepository.findById(id);
        if (byId.isPresent()) {
            Attachment attachment = byId.get();

            Optional<AttachmentContent> byAttachmentId = attachmentContentRepository.findByAttachmentId(attachment.getId());
            if (byAttachmentId.isPresent()) {
                AttachmentContent attachmentContent = byAttachmentId.get();

                // fayl nomi
                response.setHeader("Content-Disposition",
                        "attachment; filename = \""
                                + attachment.getOriginalFileName() + "\"");

                //type:
                response.setContentType(attachment.getContentType());// nima bo'lasa shuni ayatadi
                // content :
                FileCopyUtils.copy(attachmentContent.getBytes(), response.getOutputStream());

                return ResponseEntity.ok(response);

            }
        }
        return ResponseEntity.notFound().build();
    }

}
