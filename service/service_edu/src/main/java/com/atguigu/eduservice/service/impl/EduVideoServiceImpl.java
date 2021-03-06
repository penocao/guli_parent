package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 课程视频 服务实现类
 *
 * @author testjava
 * @since 2021-02-09
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo>
        implements EduVideoService {

    @Override
    public boolean deleteVideo(String videoId) {
        int result = this.baseMapper.deleteById(videoId);
        return result > 0;
    }
}
