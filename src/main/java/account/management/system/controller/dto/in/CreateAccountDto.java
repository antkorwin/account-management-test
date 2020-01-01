package account.management.system.controller.dto.in;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {
	private String name;
	private long balance;
}
