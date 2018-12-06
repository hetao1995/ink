package xyz.itao.ink.domain.params;

import lombok.Builder;
import lombok.Data;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Data
@Builder
public class AdvanceParam {
    private String cacheKey;
    private String blockIps;
    private String pluginName;
    private String cdnURL;
    private String allowInstall;
    private String allowCommentAudit;
    private String allowCloudCDN;
}
