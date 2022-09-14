package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // 오늘의 일기를 작성할때 쓰는 값들 1. LocalDate, String text 로 할때 => date는 여러개 값을 지정가능 -> @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    @ApiOperation(value = "일기 텍스트와 날씨를 이용해서 DB에 일기 저장", notes = "이것은 노트")
    @PostMapping("/create/diary")
    void createDiary(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody String text){
        diaryService.createDiary(date, text);
    }

    // 날씨 조회 API 구현
    @ApiOperation("선택한 날짜의 모든 일기 테이터를 가져옵니다")
    @GetMapping("/read/diary")
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date) {
//        if (date.isAfter(LocalDate.ofYearDay(2023,1))){
//            throw new InvalidDate();
//        }
        return diaryService.readDiary(date);
    }

    // 날짜 범위 조회 API
    @ApiOperation("선택한 기간중의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) @ApiParam(value = "조회할 기간의 첫번째날", example = "2022-02-02") LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) @ApiParam(value = "조회할 기간의 마지막날", example = "2022-02-02") LocalDate endDate){
        return diaryService.readDiaries(startDate, endDate);
    }

    // 날씨 일기 수정 API 구현
    @ApiOperation(value = "새로운 메시지 생성입니다")
    @PutMapping("/update/diary")
    void updateDiary(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody String text) {
        diaryService.updateDiary(date, text);
    }

    // 날씨 일기 삭제 API 구현
    @ApiOperation(value = "삭제 메시지 입니다")
    @DeleteMapping("/delete/diary")
    void deleteDiary(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiary(date);
    }
}

