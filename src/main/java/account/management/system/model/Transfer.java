package account.management.system.model;


import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Transfer {
	private Long id;
	private Long fromAccountId;
	private Long toAccountId;
	private BigDecimal value;
	private Date dateTime;
}
