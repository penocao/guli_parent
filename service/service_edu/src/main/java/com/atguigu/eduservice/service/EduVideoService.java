package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 课程视频 服务类
 *
 * @author testjava
 * @since 2021-02-09
 */
public interface EduVideoService extends IService<EduVideo> {

    boolean deleteVideo(String videoId);
}
