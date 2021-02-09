package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程科目 服务实现类
 *
 * @author testjava
 * @since 2021-02-03
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject>
        implements EduSubjectService {

    // 添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {

        try {
            // 文件输入流
            InputStream in = file.getInputStream();
            EasyExcel.read(in, SubjectData.class, new SubjectExcelListener(subjectService))
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 课程分类列表（树形）
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        // 创建list集合，用于存储最终封装数据
        List<OneSubject> oneSubjects = new ArrayList<>();
        // 1 查询所有一级分类
        QueryWrapper<EduSubject> oneSubjectWrapper = new QueryWrapper<>();
        oneSubjectWrapper.eq("parent_id", "0");
        List<EduSubject> oneSubjectListsTemp = baseMapper.selectList(oneSubjectWrapper);
        // 2 查询所有二级分类
        QueryWrapper<EduSubject> twoSubjectWrapper = new QueryWrapper<>();
        twoSubjectWrapper.ne("parent_id", "0");
        List<EduSubject> twoSubjectListsTemp = baseMapper.selectList(twoSubjectWrapper);

        // 3 封装一级分类
        // 查询出来所有的一级分类list集合遍历，得到每个一级分类对象，获取每个一级分类对象值
        // 封装到要求的list集合里面List<OneSubject> finalSubjectList
        for (EduSubject oneSubjectTemp : oneSubjectListsTemp) {
            OneSubject oneSubject = new OneSubject();
            //            oneSubject.setId(oneSubjectTemp.getId());
            //            oneSubject.setTitle(oneSubjectTemp.getTitle());
            BeanUtils.copyProperties(oneSubjectTemp, oneSubject);
            List<TwoSubject> twoSubjects = oneSubject.getChildren();
            // 4 封装二级分类
            for (EduSubject twoSubjectTemp : twoSubjectListsTemp) {
                if (twoSubjectTemp.getParentId().equals(oneSubject.getId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    //                twoSubject.setId(twoSubjectTemp.getId());
                    //                twoSubject.setTitle(twoSubjectTemp.getTitle());
                    BeanUtils.copyProperties(twoSubjectTemp, twoSubject);
                    twoSubjects.add(twoSubject);
                }
            }

            oneSubjects.add(oneSubject);
        }

        return oneSubjects;
    }
}
