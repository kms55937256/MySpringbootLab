package com.rookies3.MySpringbootlab.Config.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MyEnvironment {
    private String mode;
}