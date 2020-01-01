package account.management.system.controller.dto.out;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
	private Long id;
	private String name;
	private BigDecimal balance;
}
