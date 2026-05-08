package shop.util;

import java.util.ArrayList;
import java.util.List;

public class SearchUtils {
    public static int LevenshteinDistance(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                      dp[i][j - 1],
                      Math.min(
                        dp[i - 1][j],
                        dp[i - 1][j - 1]
                      )  
                    );
                }
            }
        }

        return dp[m][n];
    }

    public static List<String> LevenshteinMatches(String str, List<String> l, int tolerance) {
        List<String> matches = new ArrayList<>();
        
        for (String toMatch : l) {
            if (LevenshteinDistance(str, toMatch) <= tolerance) matches.add(toMatch);
        }

        return matches;
    }
}
