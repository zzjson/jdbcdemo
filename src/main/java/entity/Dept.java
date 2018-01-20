package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2018 soho team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : entity</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2018年01月18日</li>
 * <li>@author     : zzy0_0</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Data
@AllArgsConstructor

/**
 * select * from Dept
 */
public class Dept {
    private Long id;
    private String dept_name;
    private String role;
}