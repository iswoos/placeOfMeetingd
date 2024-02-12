package com.example.placemeeting.controller;

import com.example.placemeeting.dto.reqeustdto.PostRequest;
import com.example.placemeeting.dto.responsedto.PostResponse.PostDetailResDto;
import com.example.placemeeting.dto.responsedto.PostResponse.PostMainResDto;
import com.example.placemeeting.dto.responsedto.PostResponse.mostPopularPostResDto;
import com.example.placemeeting.global.dto.ResponseDto;
import com.example.placemeeting.security.user.UserDetailsImpl;
import com.example.placemeeting.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    // 게시물 등록하기
    @PostMapping("/posts")
    public ResponseDto<String> createPost(@RequestPart(required = false, value = "file") MultipartFile file, @RequestPart("postData") PostRequest.PostCreate postCreate, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return ResponseDto.success(postService.createPost(file, postCreate, userDetails.getAccount()));
    }

    // 카테고리, 도시에 맞는 전체 게시물 조회
    @GetMapping("/posts")
    public ResponseDto<List<PostMainResDto>> getPosts(@RequestParam("postType") String postType, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.getPosts(postType, userDetails.getAccount()));
    }

    // 게시물 단건조회
    @GetMapping("/posts/{postId}")
    public ResponseDto<PostDetailResDto> getPost(@PathVariable Long postId) {
        return ResponseDto.success(postService.getPost(postId));
    }

    // 게시물 좋아요
    @PostMapping("/posts/like/{postId}")
    public ResponseDto<String> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.likePost(postId, userDetails.getAccount()));
    }

    // 게시물 수정하기
    @PatchMapping("/posts/{postId}")
    public ResponseDto<String> modifyPost(@PathVariable Long postId, @Valid @RequestBody PostRequest.PostModify postModify, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.modifyPost(postId, postModify, userDetails.getAccount()));
    }


    // 게시물 삭제하기 (삭제여부 Enum 관리 배제, 우선 실제 삭제하는 것으로 진행)
    @DeleteMapping("/posts/{postId}")
    public ResponseDto<String> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.deletePost(postId, userDetails.getAccount()));
    }

    // 가장 인기있는 게시물, 게시물 타입당 10개 선정하여 post id및 타이틀 제목 추출
    @GetMapping("/posts/popular")
    public ResponseDto<List<mostPopularPostResDto>> mostPopularPosts() {
        return ResponseDto.success(postService.mostPopularPosts());
    }
}
