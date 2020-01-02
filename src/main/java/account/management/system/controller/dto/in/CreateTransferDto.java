package account.management.system.controller.dto.in;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransferDto {
	private Long fromAccountId;
	private Long toAccountId;
	private Long value;
}
