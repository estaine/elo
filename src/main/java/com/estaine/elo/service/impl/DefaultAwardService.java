package com.estaine.elo.service.impl;

import com.estaine.elo.dto.PlayerStats;
import com.estaine.elo.entity.Award;
import com.estaine.elo.entity.Award.AwardLevel;
import com.estaine.elo.entity.Award.AwardType;
import com.estaine.elo.format.RatingFormatter;
import com.estaine.elo.repository.AwardRepository;
import com.estaine.elo.repository.MatchRepository;
import com.estaine.elo.service.AwardService;
import com.estaine.elo.service.RatingService;
import com.estaine.elo.service.util.DateTimeUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultAwardService implements AwardService {

    private final static String DATE_FORMAT = "dd-MMM-yyyy";
    private final static int MIN_PLAYERS_RATEABLE = 3;

    private final MatchRepository matchRepository;
    private final RatingService ratingService;
    private final DateTimeUtils dateTimeUtils;
    private final AwardRepository awardRepository;
    private final RatingFormatter ratingFormatter;

    @Value("${significance.threshold}")
    private int significanceThreshold;

    @Autowired
    public DefaultAwardService(MatchRepository matchRepository, RatingService ratingService, DateTimeUtils dateTimeUtils, AwardRepository awardRepository, RatingFormatter ratingFormatter) {
        this.matchRepository = matchRepository;
        this.ratingService = ratingService;
        this.dateTimeUtils = dateTimeUtils;
        this.awardRepository = awardRepository;
        this.ratingFormatter = ratingFormatter;
    }

    @Override
    public void updateAwards() {
        awardRepository.deleteByType(AwardType.WEEK_RATING_DELTA);
        awardRepository.deleteByType(AwardType.WEEK_END_OVERALL_RATING);

        LocalDateTime start = matchRepository.findFirstByOrderByPlayedOnAsc().getPlayedOn();
        LocalDateTime end = matchRepository.findFirstByOrderByPlayedOnDesc().getPlayedOn();

        int periodInWeeks = dateTimeUtils.calculateTimePeriodInWeeks(start, end);

        for (int i = 0; i < periodInWeeks; i++) {
            LocalDateTime weekStart = dateTimeUtils.getWeekStart(start).plusWeeks(i).atStartOfDay();
            LocalDateTime weekEnd = weekStart.plusDays(7).minusSeconds(1);

            List<PlayerStats> weekDeltaRatings = ratingFormatter
                    .sortRating(ratingService.calculateRatings(weekEnd, weekStart), 1);

            if(weekDeltaRatings.size() >= MIN_PLAYERS_RATEABLE) {
                List<Award> weekDeltaAwards = buildWeekDeltaRatingAwards(weekDeltaRatings, weekStart, weekEnd);
                awardRepository.save(weekDeltaAwards);
            }

            List<PlayerStats> weekEndOverallRatings = ratingFormatter
                    .sortRating(ratingService.calculateRatings(weekEnd), significanceThreshold);

            if(weekEndOverallRatings.size() < MIN_PLAYERS_RATEABLE) {
                weekEndOverallRatings = ratingFormatter
                        .sortRating(ratingService.calculateRatings(weekEnd), 1);
            }

            List<Award> weekEndOverallAwards = buildWeekEndOverallRatingAwards(weekEndOverallRatings, weekEnd);
            awardRepository.save(weekEndOverallAwards);
        }
    }

    private List<Award> buildWeekDeltaRatingAwards(List<PlayerStats> stats, LocalDateTime start, LocalDateTime end) {
        List<Award> awards = new ArrayList<>();

        awards.add(new Award(AwardType.WEEK_RATING_DELTA, AwardLevel.GOLD,
                buildWeekDeltaRatingName(AwardLevel.GOLD, start, end, stats.get(0).getBaseStats().getRating()),
                stats.get(0).getPlayer()));
        awards.add(new Award(AwardType.WEEK_RATING_DELTA, AwardLevel.SILVER,
                buildWeekDeltaRatingName(AwardLevel.SILVER, start, end, stats.get(1).getBaseStats().getRating()),
                stats.get(1).getPlayer()));
        awards.add(new Award(AwardType.WEEK_RATING_DELTA, AwardLevel.BRONZE,
                buildWeekDeltaRatingName(AwardLevel.BRONZE, start, end, stats.get(2).getBaseStats().getRating()),
                stats.get(2).getPlayer()));

        return awards;
    }

    private List<Award> buildWeekEndOverallRatingAwards(List<PlayerStats> stats, LocalDateTime weekEnd) {
        List<Award> awards = new ArrayList<>();

        awards.add(new Award(AwardType.WEEK_END_OVERALL_RATING, AwardLevel.GOLD,
                buildWeekEndOverallRatingName(AwardLevel.GOLD, weekEnd, stats.get(0).getBaseStats().getRating()),
                stats.get(0).getPlayer()));
        awards.add(new Award(AwardType.WEEK_END_OVERALL_RATING, AwardLevel.SILVER,
                buildWeekEndOverallRatingName(AwardLevel.SILVER, weekEnd, stats.get(1).getBaseStats().getRating()),
                stats.get(1).getPlayer()));
        awards.add(new Award(AwardType.WEEK_END_OVERALL_RATING, AwardLevel.BRONZE,
                buildWeekEndOverallRatingName(AwardLevel.BRONZE, weekEnd, stats.get(2).getBaseStats().getRating()),
                stats.get(2).getPlayer()));

        return awards;
    }

    private String buildWeekDeltaRatingName(AwardLevel level, LocalDateTime start, LocalDateTime end, Double ratingDelta) {
        return getTextOrdinalByLevel(level)
                + " rank gain during week "
                + start.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                + " - "
                + end.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                + " (" + Integer.toString(ratingDelta.intValue()) + ")";
    }

    private String buildWeekEndOverallRatingName(AwardLevel level, LocalDateTime weekEnd, Double rating) {
        return getTextOrdinalByLevel(level)
                + " overall rank on "
                + weekEnd.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                + " (" + Integer.toString(rating.intValue()) + ")";
    }

    private String getTextOrdinalByLevel(AwardLevel level) {
        switch (level) {
            case GOLD:
                return "1st";
            case SILVER:
                return "2nd";
            case BRONZE:
                return "3rd";
            default:
                return "";
        }
    }
}
