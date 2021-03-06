package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程 服务实现类
 *
 * @author testjava
 * @since 2021-02-09
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter>
        implements EduChapterService {

    @Autowired private EduVideoService eduVideoService;

    // 课程大纲列表，根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        // 1 根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapters = this.baseMapper.selectList(eduChapterQueryWrapper);

        // 2 根据课程id查询课程里面所有的小节

        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideos = eduVideoService.list(eduVideoQueryWrapper);
        ArrayList<ChapterVo> chapterVos = new ArrayList<>();

        // 3 遍历查询章节list集合进行封装
        for (EduChapter educhapter : eduChapters) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(educhapter, chapterVo);
            chapterVos.add(chapterVo);
            for (EduVideo eduVideo : eduVideos) {
                List<VideoVo> videoVos = chapterVo.getChildren();
                if (eduVideo.getChapterId().equals(educhapter.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoVos.add(videoVo);
                }
            }
        }

        // 4 遍历查询小节list集合，进行封装
        return chapterVos;
    }

    // 删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        // 1 根据chapterid章节id查询小节表，如果查询数据，不进行删除
        QueryWrapper<EduVideo> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("chapter_id", chapterId);
        int count = eduVideoService.count(eduChapterQueryWrapper);
        if (count > 0) { // 查询出小节，不进行删除
            throw new GuliException(20001, "不能删除");
        } else { // 不能查询数据，进行删除
            // 删除章节
            int result = baseMapper.deleteById(chapterId);
            return result > 0;
        }
    }
}
