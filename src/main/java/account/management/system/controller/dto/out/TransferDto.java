package account.management.system.controller.dto.out;


import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransferDto {
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal value;
    private Date dateTime;
}