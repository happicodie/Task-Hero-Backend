package com.fivesigma.backend.po_entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Andy
 * @date 2022/10/28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
//@TableName("checklist_table")
public class CheckList implements Comparable<CheckList>{
    private String check_detail;
    private boolean bool_check;

    @Override
    public int compareTo(@NotNull CheckList o) {
        return this.check_detail.compareTo(o.check_detail);
    }
}
