package com.company.platform.diagnosis;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DiagnosisMapper {
    @Insert("INSERT INTO diagnosis_run(case_id,circuit_id,status,created_by,created_at,updated_at) VALUES(#{caseId},#{circuitId},#{status},#{createdBy},#{createdAt},#{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertRun(DiagnosisRun run);

    @Insert("INSERT INTO diagnosis_step(diagnosis_id,case_id,circuit_id,branch,system_name,step_name,status,started_at,finished_at,raw_output,error_message) VALUES(#{diagnosisId},#{caseId},#{circuitId},#{branch},#{systemName},#{stepName},#{status},#{startedAt},#{finishedAt},#{rawOutput},#{errorMessage})")
    void insertStep(DiagnosisStep step);

    @Select("SELECT * FROM diagnosis_run WHERE id=#{id}")
    DiagnosisRun findRun(@Param("id") long id);

    @Select("SELECT * FROM diagnosis_step WHERE diagnosis_id=#{id} ORDER BY id")
    List<DiagnosisStep> findSteps(@Param("id") long id);

    @Select("SELECT * FROM diagnosis_run WHERE circuit_id=#{circuitId} ORDER BY created_at DESC, id DESC LIMIT 100")
    List<DiagnosisRun> findByCircuitId(@Param("circuitId") String circuitId);
}
