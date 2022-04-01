package cc.ccoder.compensation.event.test.dal.mapper;

import cc.ccoder.compensation.event.test.domain.CompensationEventTestDO;
import org.apache.ibatis.annotations.*;

/**
 * @Author: congcong
 * @Email: congccoder@gmail.com
 * @Date: 2022/3/31 17:05
 */
public interface TestEventMapper {

    /**
     * 根据eventId获取事件
     *
     * @param eventId
     *            事件id
     * @return 查询结果，可能为null
     */
    @Select("select event_id, event_key, main_type, sub_type, execute_status, execute_count, extension, allow_time, error_message"
        + "  from t_compensation_event where event_id = #{eventId}")
    @Results(id = "eventMap", value = {@Result(column = "event_id", property = "eventId"),
        @Result(column = "event_key", property = "eventKey"), @Result(column = "main_type", property = "mainType"),
        @Result(column = "sub_type", property = "subType"),
        @Result(column = "execute_status", property = "executeStatus"),
        @Result(column = "execute_count", property = "executeCount"),
        @Result(column = "extension", property = "extension"), @Result(column = "allow_time", property = "allowTime"),
        @Result(column = "error_message", property = "errorMessage")})
    CompensationEventTestDO getByEventId(@Param("eventId") Long eventId);

    /**
     * 删除事件
     *
     * @param eventId
     *            事件id
     * @return 影响行数
     */
    @Delete("delete from t_compensation_event where event_id = #{eventId}")
    int deleteByEventId(@Param("eventId") Long eventId);

    /**
     * 更新状态和允许时间
     */
    @Insert("update t_compensation_event set execute_status = #{status}, allow_time = now() + interval #{delayNowSeconds} second"
        + "  where event_id = #{eventId}")
    int updateStatusAndAllowTime(@Param("eventId") Long eventId, @Param("status") String status,
        @Param("delayNowSeconds") long delayNowSeconds);

    /**
     * 删除所有event
     *
     * @return 影响行数
     */
    @Delete("delete from t_compensation_event")
    int deleteAllEvent();
}
