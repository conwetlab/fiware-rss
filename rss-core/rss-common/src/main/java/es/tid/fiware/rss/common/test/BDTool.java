/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package es.tid.fiware.rss.common.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 
 */
public final class BDTool {

    /**
     * Private constructor.
     */
    private BDTool() {
    }

    /**
     * 
     * @param packageName
     *            package name
     * @param procedureName
     *            procedure name
     * @param paramCount
     *            count for parameters
     * @return string
     */
    public static String makeCallAPlSql(final String packageName, final String procedureName, final int paramCount) {
        StringBuffer sb = new StringBuffer("{call " + packageName + "." + procedureName + "(");
        for (int n = 1; n <= paramCount; n++) {
            sb.append("?");
            if (n < paramCount) {
                sb.append(",");
            }
        }
        return sb.append(")}").toString();
    }

    /**
     * 
     * @param rs
     *            ResultSet
     * @param s
     *            Statement
     * @param c
     *            Connection
     */
    public static void close(final ResultSet rs, final Statement s, final Connection c) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (s != null) {
                s.close();
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }
}
