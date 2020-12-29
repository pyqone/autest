package pres.auxiliary.work.selenium.event.extend;

import java.util.Optional;

public interface NoParamGroupEventFunction extends GroupEventFunction {
	default Optional<Object> action(Optional<Object> arg) {
		return Optional.empty();
	}
}
