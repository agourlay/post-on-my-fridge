package com.agourlay.pomf.tools.transformer;

import com.agourlay.pomf.model.Fridge;
import com.google.common.base.Function;

public class ExtractFridgeName implements Function<Fridge, String>{

	@Override
	public String apply(Fridge fridge) {
		return fridge.getName();
	}

}
