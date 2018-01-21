package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reflection.Column;
import reflection.Id;
import reflection.Table;

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
@NoArgsConstructor
/**
 * select * from Dept
 */
@Table(tableName = "dept")
public class Dept {
    @Id
    private Long id;

    @Column(name = "dept_name")
    private String deptName;

    private String role;

}