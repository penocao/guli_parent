package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 课程视频 前端控制器
 *
 * @author testjava
 * @since 2021-02-09
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {

    @Autowired private EduVideoService eduVideoService;

    // 添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        this.eduVideoService.save(eduVideo);
        return R.ok();
    }

    // 根据id查询章节
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId) {
        EduVideo eduVideo = this.eduVideoService.getById(videoId);
        return R.ok().data("video", eduVideo);
    }

    // 删除小节
    // TODO 后面这个方法需要完善：删除小节的同时把里面的视频删掉
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId) {
        boolean flag = this.eduVideoService.removeById(videoId);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    // 修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        this.eduVideoService.updateById(eduVideo);
        return R.ok();
    }
}
