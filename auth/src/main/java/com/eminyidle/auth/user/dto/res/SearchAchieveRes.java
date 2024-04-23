package com.eminyidle.auth.user.dto.res;

import com.eminyidle.auth.user.dto.AchieveInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchAchieveRes {

    private List<AchieveInfo> acquiredAchievementList;

}
