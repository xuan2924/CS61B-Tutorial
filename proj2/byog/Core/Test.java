package byog.Core;

import byog.Core.Game;
import byog.TileEngine.TETile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Test {
    public void test() {

        // 1. 设置输入指令 (直接复制自 Autograder 的报错信息)
        // 期望：一直玩到底
        String inputFull = "n3415218040718096461ssdsddaddaad";

        // 实际：玩一半保存(:q)，然后加载(l)继续玩(d)
        String inputSave = "n3415218040718096461ssdsddaddaa:q";
        String inputLoad = "ld";

        // 2. 运行参照组 (Expected)
        Game gameRef = new Game();
        TETile[][] expectedWorld = gameRef.playWithInputString(inputFull);

        // 3. 运行测试组 (Actual)
        // 第一步：运行到保存
        Game gameSave = new Game();
        gameSave.playWithInputString(inputSave);

        // 第二步：模拟重启程序，创建新的 Game 对象进行加载
        Game gameLoad = new Game();
        TETile[][] actualWorld = gameLoad.playWithInputString(inputLoad);

        // 4. 验证结果
        assertNotNull("Expected world should not be null", expectedWorld);
        assertNotNull("Actual world (after load) should not be null", actualWorld);

        // 检查地图尺寸是否一致
        assertEquals("Width mismatch", expectedWorld.length, actualWorld.length);
        assertEquals("Height mismatch", expectedWorld[0].length, actualWorld[0].length);

        // 5. 逐个像素对比世界
        int width = expectedWorld.length;
        int height = expectedWorld[0].length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TETile expTile = expectedWorld[x][y];
                TETile actTile = actualWorld[x][y];

                // 关键：比较 Tile 的字符特征 (character) 或者是描述 (description)
                // 如果这里报错，说明这一个坐标上的东西不一样
                String errorMsg = String.format("Mismatch at x=%d, y=%d. "
                                + "Expected: '%s' (%s), Actual: '%s' (%s)",
                        x, y,
                        expTile.character(), expTile.description(),
                        actTile.character(), actTile.description());

                assertEquals(errorMsg, expTile.character(), actTile.character());
            }
        }

    }
}