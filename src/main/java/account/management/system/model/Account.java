package account.management.system.model;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class Account {
	private Long id;
	private String name;
	private BigDecimal balance;
}
