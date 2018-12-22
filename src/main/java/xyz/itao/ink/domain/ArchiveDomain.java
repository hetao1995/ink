package xyz.itao.ink.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.itao.ink.domain.entity.Archive;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-22
 * @description 归档domain
 */
@Data
@Accessors(chain = true)
public class ArchiveDomain {
    ArchiveDomain(ContentRepository contentRepository){
        this.contentRepository = contentRepository;
    }
    private ContentRepository contentRepository;
    private String dateStr;
    private Integer count;
    private String type;
    private String status;
    List<ContentDomain> contentDomains;

    public ArchiveDomain assemble(Archive entity){
        this.dateStr = entity.getDateStr();
        this.count = entity.getCount();
        return this;
    }

    public List<ContentDomain> getContentDomains() {
        if(type==null || status==null || dateStr==null){
            throw new InnerException(ExceptionEnum.ABSENT_ARGUMENT);
        }
        Date sdate = DateUtils.dateFormat(dateStr,"yyyy年MM月"), edate = DateUtils.getMonthsAfter(sdate, 1);
        Integer start = DateUtils.getUnixTimeByDate(sdate), end = DateUtils.getUnixTimeByDate(edate);
        return contentRepository.loadAllContentDomainCreatedBetween(type, status, start, end);
    }
}
