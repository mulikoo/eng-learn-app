package com.mulikoo.englearnapp.dto.view;

import com.mulikoo.englearnapp.enums.UserProgressStatus;

public interface UserProgressView {
    Long getWordId();
    UserProgressStatus getStatus();
}
