package xyz.itao.ink.domain.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hetao
 * @date 2018-12-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceParam {
    private String cacheKey;
    private String blockIps;
    private String pluginName;
    private String cdnURL;
    private String allowInstall;
    private String allowCommentAudit;
    private String allowCloudCDN;
}
