package account.management.system.controller.mapper;

import java.util.List;

import account.management.system.controller.dto.out.TransferDto;
import account.management.system.model.Transfer;
import org.mapstruct.Mapper;


@Mapper
public interface TransferMapper {

	TransferDto toDto(Transfer transfer);

	List<TransferDto> toListDto(List<Transfer> transfers);
}
