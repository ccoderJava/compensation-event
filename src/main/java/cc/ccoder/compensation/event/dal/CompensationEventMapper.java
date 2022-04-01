package cc.ccoder.compensation.event.dal;

import cc.ccoder.compensation.event.dal.domain.CompensationEventDO;
import cc.ccoder.compensation.event.dal.extend.SimpleSelectExtendedLanguageDriver;
import cc.ccoder.compensation.event.domain.CompensationEvent;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 补偿事件落库操作接口
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/29 22:40
 */
public interface CompensationEventMapper {
    /**
     * 保存补偿事件
     *
     * @param eventDO
     *            待落库补偿事件
     * @param seconds
     *            允许时间延迟
     */
    @Insert("insert into t_compensation_event (event_key, main_type, sub_type, execute_status, execute_count, extension, error_message, allow_time, create_time, update_time)"
        + " values (#{event.eventKey}, #{event.mainType}, #{event.subType}, 'I', 0, #{event.extension}, #{event.errorMessage}, now() + interval #{allowDelaySeconds} second, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "event.eventId", keyColumn = "event_id")
    void insert(@Param("event") CompensationEventDO eventDO, @Param("allowDelaySeconds") long seconds);

    /**
     * 根据业务标志eventKey获取补偿事件ID
     *
     * @param eventKey
     *            业务标志
     * @param mainType
     *            事件主类型
     * @param subType
     *            事件子类型
     * @return 事件ID
     */
    @Select("select event_id from t_compensation_event where event_key = #{eventKey} and main_type = #{mainType} and sub_type = #{subType}")
    Long getEventIdByBizKey(@Param("eventKey") String eventKey, @Param("mainType") String mainType,
        @Param("subType") String subType);

    /**
     * 根据业务标志eventKey获取补偿事件
     *
     * @param eventKey
     *            业务标注
     * @param mainType
     *            事件主类型
     * @param subType
     *            事件子类型
     * @return 补偿事件
     */
    @Select("select event_id, event_key, main_type, sub_type, execute_status, execute_count, extension, error_message"
        + " from t_compensation_event where event_key = #{eventKey} and main_type = #{mainType} and sub_type = #{subType}")
    @ResultMap("eventMap")
    CompensationEventDO getEventByBizKey(@Param("eventKey") String eventKey, @Param("mainType") String mainType,
        @Param("subType") String subType);

    /**
     * 根据eventId获取满足执行时间的事件,executeStatus=I,E
     *
     * @param eventId
     *            补偿事件ID
     * @return 查询结果, 不存在则为null
     */
    @Select("select event_id, event_key, main_type, sub_type, execute_status, execute_count, extension, error_message"
        + " from t_compensation_event where event_id = #{eventId} and execute_status in ('I', 'E') and allow_time <= now()")
    @Results(id = "eventMap", value = {@Result(column = "event_id", property = "eventId"),
        @Result(column = "event_key", property = "eventKey"), @Result(column = "main_type", property = "mainType"),
        @Result(column = "sub_type", property = "subType"),
        @Result(column = "execute_status", property = "executeStatus"),
        @Result(column = "execute_count", property = "executeCount"),
        @Result(column = "error_message", property = "errorMessage"),
        @Result(column = "extension", property = "extension"), @Result(column = "allow_time", property = "allowTime")})
    CompensationEventDO getAllowByEventId(@Param("eventId") Long eventId);

    /**
     * 删除补偿事件
     *
     * @param eventId
     *            事件ID
     * @return 受影响行数
     */
    @Delete("delete from t_compensation_event where event_id = #{eventId}")
    int deleteByEventId(@Param("eventId") Long eventId);

    /**
     * 更新补偿事件状态为失败
     *
     * @param eventId
     *            补偿事件ID
     * @param errorMessage
     *            错误信息
     * @param preStatus
     *            前置状态
     * @return 受影响行数
     */
    @Update("update t_compensation_event set execute_status = 'F', execute_count = execute_count + 1, error_message = #{errorMessage}"
        + "    , update_time = now() where event_id = #{eventId} and execute_status = #{preStatus}")
    int updateFail(@Param("eventId") Long eventId, @Param("errorMessage") String errorMessage,
        @Param("preStatus") String preStatus);

    /**
     * 更新补偿事件为异常
     *
     * @param eventId
     *            事件ID
     * @param errorMessage
     *            错误消息
     * @param allowTimeDelaySeconds
     *            允许执行时间延迟秒数
     * @param preStatus
     *            前置状态
     * @return 受影响行数
     */
    @Update("update t_compensation_event set execute_status = 'E', execute_count = execute_count + 1, error_message = #{errorMessage}"
        + "  , allow_time = now() + interval #{allowTimeDelaySeconds} second, update_time = now()"
        + "  where event_id = #{eventId} and execute_status = #{preStatus}")
    int updateError(@Param("eventId") Long eventId, @Param("errorMessage") String errorMessage,
        @Param("allowTimeDelaySeconds") long allowTimeDelaySeconds, @Param("preStatus") String preStatus);

    /**
     * 获取满足执行时间要求的补偿事件
     * 
     * @param batchSize
     *            重试批次大小
     * @param shardingIndex
     *            当前分片
     * @param shardingCount
     *            分片数
     * @return 待执行补偿事件
     */
    @Select("select event_id, event_key, main_type, sub_type, execute_status, execute_count, extension, error_message"
        + " from t_compensation_event where execute_status in ('I', 'E') and allow_time <= now() and event_id % #{shardingCount} = #{shardingIndex}"
        + " order by allow_time limit #{batchSize}")
    @ResultMap("eventMap")
    List<CompensationEventDO> listRetryEvent(@Param("batchSize") int batchSize,
        @Param("shardingIndex") int shardingIndex, @Param("shardingCount") int shardingCount);

    /**
     * 获取满足执行时间要求的补偿事件
     * 
     * @param batchSize
     *            重试批次大小
     * @param shardingIndex
     *            当前分片
     * @param shardingCount
     *            分片数
     * @param handlerCodeWhiteList
     *            白名单
     * @return 待执行补偿事件
     */
    @Lang(SimpleSelectExtendedLanguageDriver.class)
    @Select("select event_id, event_key, main_type, sub_type, execute_status, execute_count, extension, error_message"
        + "  from t_compensation_event where execute_status in ('I', 'E') and allow_time &lt;= now() "
        + "    and event_id % #{shardingCount} = #{shardingIndex} and main_type in (#{handlerCodeWhiteList})"
        + "  order by allow_time limit #{batchSize}")
    @ResultMap("eventMap")
    List<CompensationEventDO> listRetryEventWithMainTypeList(@Param("batchSize") int batchSize,
        @Param("shardingIndex") int shardingIndex, @Param("shardingCount") int shardingCount,
        @Param("handlerCodeWhiteList") List<String> handlerCodeWhiteList);

}
