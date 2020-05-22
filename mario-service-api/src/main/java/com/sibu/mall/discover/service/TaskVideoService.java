package com.mall.discover.service;

import com.doubo.common.model.response.CommonResponse;
import com.mall.discover.request.video.TaskVideo;

public interface TaskVideoService {

     CommonResponse<Boolean> saveTaskVideo(TaskVideo taskVideo,String webpUrl);
}
