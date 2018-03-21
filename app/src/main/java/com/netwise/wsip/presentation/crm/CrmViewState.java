package com.netwise.wsip.presentation.crm;

import com.google.auto.value.AutoValue;
import com.netwise.wsip.domain.crm.CrmData;
import com.netwise.wsip.domain.crm.NoteHeader;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;

import java.util.List;

import javax.annotation.Nullable;

@AutoValue
public abstract class CrmViewState {

    @Nullable
    abstract List<School> schools();

    @Nullable
    abstract List<Teacher> teachers();

    @Nullable
    abstract List<NoteHeader> noteHeaders();

    static Builder builder() {
        return new AutoValue_CrmViewState.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public static abstract class Builder {
        abstract Builder schools(List<School> schools);

        abstract Builder teachers(List<Teacher> teachers);

        abstract Builder noteHeaders(List<NoteHeader> noteHeaders);

        abstract CrmViewState build();
    }
}
